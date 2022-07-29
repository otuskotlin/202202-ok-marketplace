package ru.otus.otuskotlin.marketplace.biz.permissions

import com.crowdproj.kotlin.cor.ICorChainDsl
import com.crowdproj.kotlin.cor.handlers.chain
import com.crowdproj.kotlin.cor.handlers.worker
import ru.otus.otuskotlin.marketplace.backend.common.models.MkplUserPermissions
import ru.otus.otuskotlin.marketplace.common.MkplContext
import ru.otus.otuskotlin.marketplace.common.models.MkplSearchTypes
import ru.otus.otuskotlin.marketplace.common.models.MkplState

fun ICorChainDsl<MkplContext>.searchTypes(title: String) = chain {
    this.title = title
    description = "Добавление ограничений в поисковый запрос согласно правам доступа и др. политикам"
    on { state == MkplState.RUNNING }
    worker("Определение типа поиска") {
        adFilterValidated.searchTypes = listOf(
            MkplSearchTypes.OWN.takeIf { chainPermissions.contains(MkplUserPermissions.SEARCH_OWN) },
            MkplSearchTypes.PUBLIC.takeIf { chainPermissions.contains(MkplUserPermissions.SEARCH_PUBLIC) },
            MkplSearchTypes.REGISTERED.takeIf { chainPermissions.contains(MkplUserPermissions.SEARCH_REGISTERED) },
        ).filterNotNull().toMutableSet()
    }
}