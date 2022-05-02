package ru.otus.otuskotlin.marketplace.mappers.v2.exceptions

import ru.otus.otuskotlin.marketplace.api.v2.models.IAdProduct

class UnknownAdProduct(prod: IAdProduct) : Throwable("Cannot map unknown product $prod") {

}
