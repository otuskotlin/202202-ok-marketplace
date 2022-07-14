package ru.otus.otuskotlin.marketplace.backend.repo.cassandra.model

import ru.otus.otuskotlin.marketplace.common.models.MkplAdPermissionClient
import ru.otus.otuskotlin.marketplace.common.models.MkplDealSide

enum class AdPermissionClient {
    READ,
    UPDATE,
    DELETE,
    MAKE_VISIBLE_PUBLIC,
    MAKE_VISIBLE_GROUP,
    MAKE_VISIBLE_OWNER,
}

fun AdPermissionClient.fromTransport() = when(this) {
    AdPermissionClient.READ -> MkplAdPermissionClient.READ
    AdPermissionClient.UPDATE -> MkplAdPermissionClient.UPDATE
    AdPermissionClient.DELETE -> MkplAdPermissionClient.DELETE
    AdPermissionClient.MAKE_VISIBLE_PUBLIC -> MkplAdPermissionClient.MAKE_VISIBLE_PUBLIC
    AdPermissionClient.MAKE_VISIBLE_GROUP -> MkplAdPermissionClient.MAKE_VISIBLE_GROUP
    AdPermissionClient.MAKE_VISIBLE_OWNER -> MkplAdPermissionClient.MAKE_VISIBLE_OWNER
}

fun MkplAdPermissionClient.toTransport() = when(this) {
    MkplAdPermissionClient.READ -> AdPermissionClient.READ
    MkplAdPermissionClient.UPDATE -> AdPermissionClient.UPDATE
    MkplAdPermissionClient.DELETE -> AdPermissionClient.DELETE
    MkplAdPermissionClient.MAKE_VISIBLE_PUBLIC -> AdPermissionClient.MAKE_VISIBLE_PUBLIC
    MkplAdPermissionClient.MAKE_VISIBLE_GROUP -> AdPermissionClient.MAKE_VISIBLE_GROUP
    MkplAdPermissionClient.MAKE_VISIBLE_OWNER -> AdPermissionClient.MAKE_VISIBLE_OWNER
}
