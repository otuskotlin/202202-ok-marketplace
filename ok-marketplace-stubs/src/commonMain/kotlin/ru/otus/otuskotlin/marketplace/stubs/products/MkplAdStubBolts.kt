package ru.otus.otuskotlin.marketplace.stubs.products

import ru.otus.otuskotlin.marketplace.common.helpers.mm
import ru.otus.otuskotlin.marketplace.common.models.*
import ru.otus.otuskotlin.marketplace.common.models.product.MkplAdProductBolt
import ru.otus.otuskotlin.marketplace.common.models.product.MkplAdProductBoltHeadStyle
import ru.otus.otuskotlin.marketplace.common.models.product.MkplAdProductBoltThread

object MkplAdStubBolts {
    val AD_DEMAND_BOLT1: MkplAd
        get() = MkplAd(
            id = MkplAdId("666"),
            title = "Требуется болт",
            description = "Требуется болт 100x5 с шистигранной шляпкой",
            ownerId = MkplUserId("user-1"),
            adType = MkplDealSide.DEMAND,
            visibility = MkplVisibility.VISIBLE_PUBLIC,
            product = MkplAdProductBolt(
                length = 100.mm,
                diameter = 5.mm,
                headStyle = MkplAdProductBoltHeadStyle.INDENTED_HEXAGON,
                thread = MkplAdProductBoltThread(
                    pitch = 0.5.mm,
                    pitchConf = MkplAdProductBoltThread.PitchConf.FINE
                )
            ),
            permissionsClient = mutableSetOf(
                MkplAdPermissionClient.READ,
                MkplAdPermissionClient.UPDATE,
                MkplAdPermissionClient.DELETE,
                MkplAdPermissionClient.MAKE_VISIBLE_PUBLIC,
                MkplAdPermissionClient.MAKE_VISIBLE_GROUP,
                MkplAdPermissionClient.MAKE_VISIBLE_OWNER,
            )
        )
}
