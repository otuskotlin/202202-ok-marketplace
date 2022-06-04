package ru.otus.otuskotlin.marketplace.biz.validation

import com.crowdproj.kotlin.cor.ICorChainDsl
import com.crowdproj.kotlin.cor.handlers.worker
import ru.otus.otuskotlin.marketplace.biz.errors.errorValidation
import ru.otus.otuskotlin.marketplace.common.MkplContext
import ru.otus.otuskotlin.marketplace.common.helpers.fail

fun ICorChainDsl<MkplContext>.validateDescriptionHasContent(title: String) = worker {
    this.title = title
    val regExp = Regex("\\p{L}")
    on { adValidating.description.isNotEmpty() && ! adValidating.description.contains(regExp) }
    handle {
        fail(errorValidation(
            field = "description",
            violationCode = "noContent",
            description = "field must contain leters"
        ))
    }
}
