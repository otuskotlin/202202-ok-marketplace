package ru.otus.otuskotlin.marketplace.v2

import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.serialization.*
import io.ktor.util.reflect.*
import io.ktor.utils.io.*
import io.ktor.utils.io.charsets.*
import ru.otus.otuskotlin.marketplace.api.v2.apiV2RequestDeserialize
import ru.otus.otuskotlin.marketplace.api.v2.apiV2RequestSerialize
import ru.otus.otuskotlin.marketplace.api.v2.apiV2ResponseDeserialize
import ru.otus.otuskotlin.marketplace.api.v2.apiV2ResponseSerialize
import ru.otus.otuskotlin.marketplace.api.v2.models.*
import kotlin.reflect.KClass
import kotlin.reflect.KFunction

class CustomJsonConverter() : ContentConverter {
    override suspend fun deserialize(charset: Charset, typeInfo: TypeInfo, content: ByteReadChannel): Any? {
        val stringList = mutableListOf<String>()
        while(! content.isClosedForRead) {
            content.readUTF8Line()?.also { stringList.add(it) }
        }
        val jsonString = stringList.joinToString("")
        return when (typeInfo.type) {
            // Плохое решение, надо придумать что-то покрасивее
            // В Native не работают многие функции рефлексии типа isSubclassOf
            AdCreateRequest::class,
            AdReadRequest::class,
            AdUpdateRequest::class,
            AdDeleteRequest::class,
            AdSearchRequest::class,
            AdOffersRequest::class,
            -> apiV2RequestDeserialize<IRequest>(jsonString)
            AdCreateResponse::class,
            AdReadResponse::class,
            AdUpdateResponse::class,
            AdDeleteResponse::class,
            AdSearchResponse::class,
            AdOffersResponse::class,
            -> apiV2ResponseDeserialize<IResponse>(jsonString)
            else -> throw UnsupportedClassSerialization(typeInfo.type, this@CustomJsonConverter::deserialize)
        }
    }

    override suspend fun serialize(
        contentType: ContentType,
        charset: Charset,
        typeInfo: TypeInfo,
        value: Any,
    ): OutgoingContent {
        return TextContent(when (value) {
            is IRequest -> apiV2RequestSerialize(value)
            is IResponse -> apiV2ResponseSerialize(value)
            else -> throw UnsupportedClassSerialization(value::class, this@CustomJsonConverter::serialize)
        }, contentType.withCharsetIfNeeded(charset))
    }

    class NoDataException() : Throwable(message = "Empty data read by ${CustomJsonConverter::class.simpleName}")
    class UnsupportedClassSerialization(clazz: KClass<*>, handler: KFunction<*>) :
        Throwable(message = "Unsupported type expected in ${handler.name}: ${clazz.simpleName}")
}
