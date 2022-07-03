package ru.otus.otuskotlin.marketplace.biz.repo

import com.crowdproj.kotlin.cor.ICorChainDsl
import com.crowdproj.kotlin.cor.handlers.worker
import ru.otus.otuskotlin.marketplace.common.MkplContext
import ru.otus.otuskotlin.marketplace.common.helpers.errorConcurrency
import ru.otus.otuskotlin.marketplace.common.helpers.fail
import ru.otus.otuskotlin.marketplace.common.models.MkplState

fun ICorChainDsl<MkplContext>.repoCheckReadLock(title: String) = worker {
    this.title = title
    description = """
        Проверяем, что блокировка из запроса совпадает с блокировкой в БД
    """.trimIndent()
    on { state == MkplState.RUNNING && adValidated.lock != adRepoRead.lock }
    handle {
        fail(errorConcurrency(violationCode = "changed", "Object has been inconsistently changed"))
        adRepoDone = adRepoRead
    }
}
