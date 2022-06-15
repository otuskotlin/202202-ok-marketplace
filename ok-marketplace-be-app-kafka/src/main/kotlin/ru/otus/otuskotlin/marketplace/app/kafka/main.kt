package ru.otus.otuskotlin.marketplace.app.kafka

fun main() {
    val config = AppKafkaConfig(mapOf("v1" to null, "v2" to null))
    val consumer = AppKafkaConsumer(config, mapOf("v1" to ConsumerStrategyV1(), "v2" to ConsumerStrategyV2()))
    consumer.run()
}
