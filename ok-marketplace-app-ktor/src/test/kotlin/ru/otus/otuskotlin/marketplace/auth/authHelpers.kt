package ru.otus.otuskotlin.marketplace.auth

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.client.request.*
import io.ktor.http.*
import ru.otus.otuskotlin.marketplace.app.config.KtorAuthConfig

fun HttpRequestBuilder.addAuth(id: String = "user1", groups: List<String> = listOf("USER"), config: KtorAuthConfig = KtorAuthConfig.TEST) {
    val token = JWT.create()
        .withAudience(config.audience)
        .withIssuer(config.issuer)
        .withClaim(KtorAuthConfig.GROUPS_CLAIM, groups)
        .withClaim(KtorAuthConfig.ID_CLAIM, id)
        .sign(Algorithm.HMAC256(config.secret))

    header(HttpHeaders.Authorization, "Bearer $token")
}