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
import ru.otus.otuskotlin.marketplace.api.v1.apiV1RequestDeserialize
import ru.otus.otuskotlin.marketplace.api.v1.apiV1ResponseSerialize
import ru.otus.otuskotlin.marketplace.api.v1.models.IRequest
import ru.otus.otuskotlin.marketplace.api.v1.models.IResponse
import ru.otus.otuskotlin.marketplace.backend.services.AdService
import ru.otus.otuskotlin.marketplace.common.MkplContext
import ru.otus.otuskotlin.marketplace.mappers.v1.fromTransport
import ru.otus.otuskotlin.marketplace.mappers.v1.toTransportAd
import java.time.Duration
import java.util.*

private val log = KotlinLogging.logger {}

class AppKafkaConsumer(private val config: AppKafkaConfig,
                       private val service: AdService = AdService(),
                       private val consumer: Consumer<String, String> = config.createKafkaConsumer(),
                       private val producer: Producer<String, String> = config.createKafkaProducer()
) {
    private val process = atomic(true) // пояснить

    fun run() = runBlocking {
        try {
            consumer.subscribe(listOf(config.kafkaTopicIn))
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
                        log.info { "process ${record.key()}:\n${record.value()}" }
                        val request: IRequest = apiV1RequestDeserialize(record.value())
                        ctx.fromTransport(request)
                        service.exec(ctx)

                        val response = ctx.toTransportAd()
                        sendResponse(response)
                    } catch (ex : Exception) {
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

    private fun sendResponse(response: IResponse) {
        val json = apiV1ResponseSerialize(response)
        val resRecord = ProducerRecord(
            config.kafkaTopicOut,
            UUID.randomUUID().toString(),
            json
        )
        log.info { "sending ${resRecord.key()}:\n$json" }
        producer.send(resRecord)
    }

    fun stop() {
        process.value = false
    }
}
