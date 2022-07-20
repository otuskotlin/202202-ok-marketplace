package ru.otus.otuskotlin.marketplace.backend.repository.gremlin

object AdGremlinConst {
    const val RESULT_SUCCESS = "success"
    const val RESULT_LOCK_FAILURE = "lock-failure"

    const val FIELD_TITLE = "title"
    const val FIELD_DESCRIPTION = "description"
    const val FIELD_AD_TYPE = "adType"
    const val FIELD_OWNER_ID = "ownerId"
    const val FIELD_VISIBILITY = "visibility"
    const val FIELD_LOCK = "lock"

    const val FIELD_PRODUCT_TYPE = "product.type"
    const val FIELD_BOLD_HEAD_STYLE = "product.headStyle"
    const val FIELD_BOLT_LENGTH_LEN = "product.lengh.len"
    const val FIELD_BOLT_LENGTH_UNIT = "product.lengh.unit"
    const val FIELD_BOLT_DIAMETER_LEN = "product.diameter.len"
    const val FIELD_BOLT_DIAMETER_UNIT = "product.diameter.unit"
    const val FIELD_BOLT_THREAD_PITCH_LEN = "product.thread.pitch.len"
    const val FIELD_BOLT_THREAD_PITCH_UNIT = "product.thread.pitch.unit"
    const val FIELD_BOLT_THREAD_PITCH_CONF = "product.thread.pitchConf"
}
