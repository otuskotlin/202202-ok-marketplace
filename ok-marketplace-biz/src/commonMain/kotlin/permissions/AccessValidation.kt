package ru.otus.otuskotlin.marketplace.biz.permissions

import ru.otus.otuskotlin.marketplace.common.MkplContext
import com.crowdproj.kotlin.cor.ICorChainDsl
import com.crowdproj.kotlin.cor.handlers.chain
import com.crowdproj.kotlin.cor.handlers.worker
import ru.otus.otuskotlin.marketplace.backend.common.models.MkplPrincipalModel
import ru.otus.otuskotlin.marketplace.backend.common.models.MkplPrincipalRelations
import ru.otus.otuskotlin.marketplace.common.helpers.addError
import ru.otus.otuskotlin.marketplace.common.models.MkplAd
import ru.otus.otuskotlin.marketplace.common.models.MkplError
import ru.otus.otuskotlin.marketplace.common.models.MkplState
import ru.otus.otuskotlin.marketplace.common.models.MkplVisibility

fun ICorChainDsl<MkplContext>.accessValidation(title: String) = chain {
    this.title = title
    description = "Вычисление прав доступа по группе принципала и таблице прав доступа"
    on { state == MkplState.RUNNING }
    worker("Вычисление отношения объявления к принципалу") {
        adRepoRead.principalRelations = adRepoRead.resolveRelationsTo(principal)
    }
    worker("Вычисление доступа к объявлению") {
        permitted = adRepoRead.principalRelations.asSequence().flatMap { relation ->
            chainPermissions.map { permission ->
                AccessTableConditions(
                    command = command,
                    permission = permission,
                    relation = relation,
                )
            }
        }.any {
            accessTable[it] ?: false
        }
    }
    worker {
        this.title = "Валидация прав доступа"
        description = "Проверка наличия прав для выполнения операции"
        on { !permitted }
        handle {
            addError(
                MkplError(message = "User is not allowed to this operation")
            )
        }
    }
}

private fun MkplAd.resolveRelationsTo(principal: MkplPrincipalModel): Set<MkplPrincipalRelations> = listOf(
    MkplPrincipalRelations.NONE,
    MkplPrincipalRelations.OWN.takeIf { principal.id == ownerId },
    MkplPrincipalRelations.PUBLIC.takeIf { visibility == MkplVisibility.VISIBLE_PUBLIC },
    MkplPrincipalRelations.MODERATABLE.takeIf { visibility != MkplVisibility.VISIBLE_TO_OWNER },
).filterNotNull().toSet()