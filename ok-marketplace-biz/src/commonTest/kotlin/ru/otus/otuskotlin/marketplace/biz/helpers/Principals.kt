package ru.otus.otuskotlin.marketplace.biz.helpers

import ru.otus.otuskotlin.marketplace.backend.common.models.MkplPrincipalModel
import ru.otus.otuskotlin.marketplace.backend.common.models.MkplUserGroups
import ru.otus.otuskotlin.marketplace.common.models.MkplUserId
import ru.otus.otuskotlin.marketplace.stubs.Bolt

fun principalUser(id: MkplUserId = Bolt.getModel().ownerId, banned: Boolean = false) = MkplPrincipalModel(
    id = id,
    groups = setOf(
        MkplUserGroups.USER,
        MkplUserGroups.TEST,
        if (banned) MkplUserGroups.BAN_AD else null
    )
        .filterNotNull()
        .toSet()
)

fun principalModer(id: MkplUserId = Bolt.getModel().ownerId, banned: Boolean = false) = MkplPrincipalModel(
    id = id,
    groups = setOf(
        MkplUserGroups.MODERATOR_MP,
        MkplUserGroups.TEST,
        if (banned) MkplUserGroups.BAN_AD else null
    )
        .filterNotNull()
        .toSet()
)

fun principalAdmin(id: MkplUserId = Bolt.getModel().ownerId, banned: Boolean = false) = MkplPrincipalModel(
    id = id,
    groups = setOf(
        MkplUserGroups.ADMIN_AD,
        MkplUserGroups.TEST,
        if (banned) MkplUserGroups.BAN_AD else null
    )
        .filterNotNull()
        .toSet()
)
