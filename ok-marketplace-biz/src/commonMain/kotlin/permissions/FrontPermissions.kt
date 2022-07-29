package ru.otus.otuskotlin.marketplace.biz.permissions

import com.crowdproj.kotlin.cor.ICorChainDsl
import com.crowdproj.kotlin.cor.handlers.chain
import com.crowdproj.kotlin.cor.handlers.worker
import ru.otus.otuskotlin.marketplace.backend.common.models.MkplUserGroups
import ru.otus.otuskotlin.marketplace.common.MkplContext
import ru.otus.otuskotlin.marketplace.common.models.MkplAdPermissionClient
import ru.otus.otuskotlin.marketplace.common.models.MkplState

fun ICorChainDsl<MkplContext>.frontPermissions(title: String) = chain {
    this.title = title
    description = "Вычисление разрешений пользователей для фронтенда"

    on { state == MkplState.RUNNING }

    worker {
        this.title = "Разрешения для собственного объявления"
        description = this.title
        on { adRepoDone.ownerId == principal.id }
        handle {
            val permAdd: Set<MkplAdPermissionClient> = principal.groups.map {
                when (it) {
                    MkplUserGroups.USER -> setOf(
                        MkplAdPermissionClient.READ,
                        MkplAdPermissionClient.UPDATE,
                        MkplAdPermissionClient.DELETE,
                    )
                    MkplUserGroups.MODERATOR_MP -> setOf()
                    MkplUserGroups.ADMIN_AD -> setOf()
                    MkplUserGroups.TEST -> setOf()
                    MkplUserGroups.BAN_AD -> setOf()
                }
            }.flatten().toSet()
            val permDel: Set<MkplAdPermissionClient> = principal.groups.map {
                when (it) {
                    MkplUserGroups.USER -> setOf()
                    MkplUserGroups.MODERATOR_MP -> setOf()
                    MkplUserGroups.ADMIN_AD -> setOf()
                    MkplUserGroups.TEST -> setOf()
                    MkplUserGroups.BAN_AD -> setOf(
                        MkplAdPermissionClient.UPDATE,
                        MkplAdPermissionClient.DELETE,
                    )
                }
            }.flatten().toSet()
            adRepoDone.permissionsClient.addAll(permAdd)
            adRepoDone.permissionsClient.removeAll(permDel)
        }
    }

    worker {
        this.title = "Разрешения для модератора"
        description = this.title
        on { adRepoDone.ownerId != principal.id /* && tag, group, ... */ }
        handle {
            val permAdd: Set<MkplAdPermissionClient> = principal.groups.map {
                when (it) {
                    MkplUserGroups.USER -> setOf()
                    MkplUserGroups.MODERATOR_MP -> setOf(
                        MkplAdPermissionClient.READ,
                        MkplAdPermissionClient.UPDATE,
                        MkplAdPermissionClient.DELETE,
                    )
                    MkplUserGroups.ADMIN_AD -> setOf()
                    MkplUserGroups.TEST -> setOf()
                    MkplUserGroups.BAN_AD -> setOf()
                }
            }.flatten().toSet()
            val permDel: Set<MkplAdPermissionClient> = principal.groups.map {
                when (it) {
                    MkplUserGroups.USER -> setOf(
                        MkplAdPermissionClient.UPDATE,
                        MkplAdPermissionClient.DELETE,
                    )
                    MkplUserGroups.MODERATOR_MP -> setOf()
                    MkplUserGroups.ADMIN_AD -> setOf()
                    MkplUserGroups.TEST -> setOf()
                    MkplUserGroups.BAN_AD -> setOf(
                        MkplAdPermissionClient.UPDATE,
                        MkplAdPermissionClient.DELETE,
                    )
                }
            }.flatten().toSet()
            adRepoDone.permissionsClient.addAll(permAdd)
            adRepoDone.permissionsClient.removeAll(permDel)
        }
    }
    worker {
        this.title = "Разрешения для администратора"
        description = this.title
    }
}
