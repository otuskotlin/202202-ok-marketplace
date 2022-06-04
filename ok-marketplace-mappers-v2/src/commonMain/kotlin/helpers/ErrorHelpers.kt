package ru.otus.otuskotlin.marketplace.mappers.v2.helpers

import ru.otus.otuskotlin.marketplace.common.models.MkplError
import ru.otus.otuskotlin.marketplace.mappers.v2.MapContext

internal fun MapContext.fail(err: MkplError) {
    errors.add(err)
    state = MapContext.MapState.FAILING
}
