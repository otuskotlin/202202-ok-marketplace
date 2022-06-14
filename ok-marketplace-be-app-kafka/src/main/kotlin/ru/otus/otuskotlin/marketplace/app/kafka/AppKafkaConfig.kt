package ru.otus.otuskotlin.marketplace.app.kafka

data class AppKafkaConfig(
    val kafkaHosts: List<String> = KAFKA_HOSTS,
    val kafkaTopicIn: String = KAFKA_TOPIC_IN,
    val kafkaTopicOut: String = KAFKA_TOPIC_OUT,
    val kafkaGroupId: String = KAFKA_GROUP_ID
) {
    companion object {
        const val KAFKA_HOST_VAR = "KAFKA_HOSTS"
        const val KAFKA_TOPIC_IN_VAR = "KAFKA_TOPIC_IN"
        const val KAFKA_TOPIC_OUT_VAR = "KAFKA_TOPIC_OUT"
        const val KAFKA_GROUP_ID_VAR = "KAFKA_GROUP_ID"

        val KAFKA_HOSTS by lazy { (System.getenv(KAFKA_HOST_VAR) ?: "").split("\\s*[,;]\\s*") }
        val KAFKA_TOPIC_IN by lazy { System.getenv(KAFKA_TOPIC_IN_VAR) ?: "marketplace-in" }
        val KAFKA_TOPIC_OUT by lazy { System.getenv(KAFKA_TOPIC_OUT_VAR) ?: "marketplace-out" }
        val KAFKA_GROUP_ID by lazy { System.getenv(KAFKA_GROUP_ID_VAR) ?: "marketplace" }
    }
}
