package ru.otus.otuskotlin.marketplace.biz.repo

import com.crowdproj.kotlin.cor.ICorChainDsl
import com.crowdproj.kotlin.cor.handlers.worker
import ru.otus.otuskotlin.marketplace.common.MkplContext
import ru.otus.otuskotlin.marketplace.common.models.MkplState

fun ICorChainDsl<MkplContext>.repoPrepareUpdate(title: String) = worker {
    this.title = title
    description = "Готовим данные к сохранению в БД: совмещаем данные, прочитанные из БД, " +
            "и данные, полученные от пользователя"
    on { state == MkplState.RUNNING }
    handle {
        adRepoPrepare = adRepoRead.deepCopy().apply {
            this.title = adValidated.title
            description = adValidated.description
            product = adValidated.product.deepCopy()
        }
    }
}
