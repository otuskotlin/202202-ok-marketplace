package ru.otus.otuskotlin.marketplace.app.kafka

data class InputOutputTopics(val input: String, val output: String)

class AppKafkaConfig(
    topicsByVersion: Map<String, InputOutputTopics?>,
    val kafkaHosts: List<String> = KAFKA_HOSTS,
    val kafkaGroupId: String = KAFKA_GROUP_ID
) {
    val topicsByVersion = topicsByVersion.mapValues { (version, topics) ->
        topics ?: InputOutputTopics(
            inputTopic(version),
            outputTopic(version)
        )
    }

    companion object {
        const val KAFKA_HOST_VAR = "KAFKA_HOSTS"
        const val KAFKA_TOPIC_IN_VAR_PREFIX = "KAFKA_TOPIC_IN_"
        const val KAFKA_TOPIC_OUT_V_VAR_PREFIX = "KAFKA_TOPIC_OUT_"
        const val KAFKA_GROUP_ID_VAR = "KAFKA_GROUP_ID"

        val KAFKA_HOSTS by lazy { (System.getenv(KAFKA_HOST_VAR) ?: "").split("\\s*[,;]\\s*") }
        val KAFKA_GROUP_ID by lazy { System.getenv(KAFKA_GROUP_ID_VAR) ?: "marketplace" }
        fun inputTopic(version: String) =
            System.getenv("$KAFKA_TOPIC_IN_VAR_PREFIX$version") ?: "marketplace-in-$version"
        fun outputTopic(version: String) =
            System.getenv("$KAFKA_TOPIC_OUT_V_VAR_PREFIX$version") ?: "marketplace-out-$version"
    }
}
