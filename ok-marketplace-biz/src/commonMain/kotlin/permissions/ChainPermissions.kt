package ru.otus.otuskotlin.marketplace.biz.permissions

import com.crowdproj.kotlin.cor.ICorChainDsl
import com.crowdproj.kotlin.cor.handlers.worker
import ru.otus.otuskotlin.marketplace.backend.common.models.MkplUserGroups
import ru.otus.otuskotlin.marketplace.backend.common.models.MkplUserPermissions
import ru.otus.otuskotlin.marketplace.common.MkplContext
import ru.otus.otuskotlin.marketplace.common.models.MkplState


fun ICorChainDsl<MkplContext>.chainPermissions(title: String) = worker {
    this.title = title
    description = "Вычисление прав доступа для групп пользователей"

    on {
        state == MkplState.RUNNING
    }

    handle {
        val permAdd: Set<MkplUserPermissions> = principal.groups.map {
            when(it) {
                MkplUserGroups.USER -> setOf(
                    MkplUserPermissions.READ_OWN,
                    MkplUserPermissions.READ_PUBLIC,
                    MkplUserPermissions.CREATE_OWN,
                    MkplUserPermissions.UPDATE_OWN,
                    MkplUserPermissions.DELETE_OWN,
                    MkplUserPermissions.OFFER_FOR_OWN,
                )
                MkplUserGroups.MODERATOR_MP -> setOf()
                MkplUserGroups.ADMIN_AD -> setOf()
                MkplUserGroups.TEST -> setOf()
                MkplUserGroups.BAN_AD -> setOf()
            }
        }.flatten().toSet()
        val permDel: Set<MkplUserPermissions> = principal.groups.map {
            when(it) {
                MkplUserGroups.USER -> setOf()
                MkplUserGroups.MODERATOR_MP -> setOf()
                MkplUserGroups.ADMIN_AD -> setOf()
                MkplUserGroups.TEST -> setOf()
                MkplUserGroups.BAN_AD -> setOf(
                    MkplUserPermissions.UPDATE_OWN,
                    MkplUserPermissions.CREATE_OWN,
                )
            }
        }.flatten().toSet()
        chainPermissions.addAll(permAdd)
        chainPermissions.removeAll(permDel)
        println("PRINCIPAL: $principal")
        println("PERMISSIONS: $chainPermissions")
    }
}
