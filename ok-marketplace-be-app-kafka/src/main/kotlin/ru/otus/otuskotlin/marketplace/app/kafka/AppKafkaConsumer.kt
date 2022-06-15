package ru.otus.otuskotlin.marketplace.app.kafka

import kotlinx.atomicfu.atomic
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock
import mu.KotlinLogging
import org.apache.kafka.clients.consumer.Consumer
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.clients.consumer.ConsumerRecords
import org.apache.kafka.clients.producer.Producer
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.common.errors.WakeupException
import ru.otus.otuskotlin.marketplace.backend.services.AdService
import ru.otus.otuskotlin.marketplace.common.MkplContext
import java.time.Duration
import java.util.*

private val log = KotlinLogging.logger {}

interface ConsumerStrategy {
    fun serialize(source: MkplContext): String
    fun deserialize(value: String, target: MkplContext)
}

class AppKafkaConsumer(
    private val config: AppKafkaConfig,
    consumerStrategyByVersion: Map<String, ConsumerStrategy>,
    private val service: AdService = AdService(),
    private val consumer: Consumer<String, String> = config.createKafkaConsumer(),
    private val producer: Producer<String, String> = config.createKafkaProducer()
) {
    init {
        assert(consumerStrategyByVersion.keys == config.topicsByVersion.keys) {
            "Strategies and topics versions must be equal: ${consumerStrategyByVersion.keys} <-> ${config.topicsByVersion.keys}"
        }
    }

    private val process = atomic(true) // пояснить
    private val topicsAndStrategyByInputTopic = config.topicsByVersion.entries.associate { (version, topics) ->
        Pair(
            topics.input,
            TopicsAndStrategy(topics.input, topics.output, consumerStrategyByVersion[version]!!)
        )
    }

    fun run() = runBlocking {
        try {
            consumer.subscribe(config.topicsByVersion.values.map { it.input })
            while (process.value) {
                val ctx = MkplContext(
                    timeStart = Clock.System.now(),
                )
                val records: ConsumerRecords<String, String> = withContext(Dispatchers.IO) {
                    consumer.poll(Duration.ofSeconds(1))
                }
                if (!records.isEmpty)
                    log.info { "Receive ${records.count()} messages" }

                records.forEach { record: ConsumerRecord<String, String> ->
                    try {
                        log.info { "process ${record.key()} from ${record.topic()}:\n${record.value()}" }
                        val (_, outputTopic, strategy) = topicsAndStrategyByInputTopic[record.topic()] ?: throw RuntimeException("Receive message from unknown topic ${record.topic()}")

                        strategy.deserialize(record.value(), ctx)
                        service.exec(ctx)

                        sendResponse(ctx, strategy, outputTopic)
                    } catch (ex: Exception) {
                        log.error(ex) { "error" }
                    }
                }
            }
        } catch (ex: WakeupException) {
            // ignore for shutdown
        } catch (ex: RuntimeException) {
            // exception handling
            withContext(NonCancellable) {
                throw ex
            }
        } finally {
            withContext(NonCancellable) {
                consumer.close()
            }
        }
    }

    private fun sendResponse(context: MkplContext, strategy: ConsumerStrategy, outputTopic: String) {
        val json = strategy.serialize(context)
        val resRecord = ProducerRecord(
            outputTopic,
            UUID.randomUUID().toString(),
            json
        )
        log.info { "sending ${resRecord.key()} to $outputTopic:\n$json" }
        producer.send(resRecord)
    }

    fun stop() {
        process.value = false
    }

    private data class TopicsAndStrategy(val inputTopic: String, val outputTopic: String, val strategy: ConsumerStrategy)
}
