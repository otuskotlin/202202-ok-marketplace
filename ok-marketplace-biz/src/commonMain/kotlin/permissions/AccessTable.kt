package ru.otus.otuskotlin.marketplace.biz.permissions

import ru.otus.otuskotlin.marketplace.backend.common.models.MkplPrincipalRelations
import ru.otus.otuskotlin.marketplace.backend.common.models.MkplUserPermissions
import ru.otus.otuskotlin.marketplace.common.models.MkplCommand

data class AccessTableConditions(
    val command: MkplCommand,
    val permission: MkplUserPermissions,
    val relation: MkplPrincipalRelations
)

val accessTable = mapOf(
    // Create
    AccessTableConditions(
        command = MkplCommand.CREATE,
        permission = MkplUserPermissions.CREATE_OWN,
        relation = MkplPrincipalRelations.NONE
    ) to true,

    // Read
    AccessTableConditions(
        command = MkplCommand.READ,
        permission = MkplUserPermissions.READ_OWN,
        relation = MkplPrincipalRelations.OWN
    ) to true,
    AccessTableConditions(
        command = MkplCommand.READ,
        permission = MkplUserPermissions.READ_PUBLIC,
        relation = MkplPrincipalRelations.PUBLIC
    ) to true,

    // Update
    AccessTableConditions(
        command = MkplCommand.UPDATE,
        permission = MkplUserPermissions.UPDATE_OWN,
        relation = MkplPrincipalRelations.OWN
    ) to true,

    // Delete
    AccessTableConditions(
        command = MkplCommand.DELETE,
        permission = MkplUserPermissions.DELETE_OWN,
        relation = MkplPrincipalRelations.OWN
    ) to true,

    // Offers
    AccessTableConditions(
        command = MkplCommand.OFFERS,
        permission = MkplUserPermissions.OFFER_FOR_OWN,
        relation = MkplPrincipalRelations.NONE
    ) to true,
)
