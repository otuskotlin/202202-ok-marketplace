package ru.otus.otuskotlin.marketplace.app.mappers

import io.ktor.server.auth.jwt.*
import ru.otus.otuskotlin.marketplace.backend.common.models.MkplPrincipalModel
import ru.otus.otuskotlin.marketplace.backend.common.models.MkplUserGroups
import ru.otus.otuskotlin.marketplace.common.models.MkplUserId

fun JWTPrincipal?.toModel() = this?.run {
    MkplPrincipalModel(
        id = payload.getClaim("id").asString()?.let { MkplUserId(it) } ?: MkplUserId.NONE,
        fname = payload.getClaim("fname").asString() ?: "",
        mname = payload.getClaim("mname").asString() ?: "",
        lname = payload.getClaim("lname").asString() ?: "",
        groups = payload
            .getClaim("groups")
            ?.asList(String::class.java)
            ?.mapNotNull {
                try {
                    MkplUserGroups.valueOf(it)
                } catch (e: Throwable) {
                    null
                }
            }?.toSet() ?: emptySet()
    )
} ?: MkplPrincipalModel.NONE
