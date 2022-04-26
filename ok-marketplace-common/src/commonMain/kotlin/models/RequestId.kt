package ru.otus.otuskotlin.marketplace.common.models

@JvmInline
value class RequestId(val id: String) {
    fun asString() = id

    companion object {
        val NONE = RequestId("")
    }
}
