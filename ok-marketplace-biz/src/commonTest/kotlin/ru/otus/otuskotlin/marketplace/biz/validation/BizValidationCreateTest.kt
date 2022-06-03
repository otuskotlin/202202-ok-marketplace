package ru.otus.otuskotlin.marketplace.biz.validation

import kotlinx.coroutines.ExperimentalCoroutinesApi
import ru.otus.otuskotlin.marketplace.biz.MkplAdProcessor
import ru.otus.otuskotlin.marketplace.common.models.MkplCommand
import kotlin.test.Test

@OptIn(ExperimentalCoroutinesApi::class)
class BizValidationCreateTest {

    private val processor = MkplAdProcessor()

    @Test fun emptyTitle() = validationTitleEmpty(MkplCommand.CREATE, processor)
    @Test fun badSymbolsTitle() = validationTitleSymbols(MkplCommand.CREATE, processor)

    @Test fun emptyDescription() = validationDescriptionEmpty(MkplCommand.CREATE, processor)

}

