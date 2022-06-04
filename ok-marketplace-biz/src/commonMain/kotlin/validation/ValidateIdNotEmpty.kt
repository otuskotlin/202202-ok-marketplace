package ru.otus.otuskotlin.marketplace.biz.validation

import com.crowdproj.kotlin.cor.ICorChainDsl
import com.crowdproj.kotlin.cor.handlers.worker
import ru.otus.otuskotlin.marketplace.biz.errors.errorValidation
import ru.otus.otuskotlin.marketplace.common.MkplContext
import ru.otus.otuskotlin.marketplace.common.helpers.fail

fun ICorChainDsl<MkplContext>.validateIdNotEmpty(title: String) = worker {
    this.title = title
    on { adValidating.id.asString().isEmpty() }
    handle {
        fail(errorValidation(
            field = "id",
            violationCode = "empty",
            description = "field must not be empty"
        ))
    }
}
