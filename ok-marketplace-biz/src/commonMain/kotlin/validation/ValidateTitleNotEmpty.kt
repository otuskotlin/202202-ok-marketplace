package ru.otus.otuskotlin.marketplace.biz.validation

import com.crowdproj.kotlin.cor.ICorChainDsl
import com.crowdproj.kotlin.cor.handlers.worker
import ru.otus.otuskotlin.marketplace.biz.errors.errorValidation
import ru.otus.otuskotlin.marketplace.common.MkplContext
import ru.otus.otuskotlin.marketplace.common.helpers.fail

fun ICorChainDsl<MkplContext>.validateTitleNotEmpty(title: String) = worker {
    this.title = title
    on { adValidating.title.isEmpty() }
    handle {
        fail(errorValidation(
            field = "title",
            violationCode = "empty",
            description = "field must not be empty"
        ))
    }
}
