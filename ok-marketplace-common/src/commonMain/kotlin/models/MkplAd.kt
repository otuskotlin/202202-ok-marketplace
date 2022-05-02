package ru.otus.otuskotlin.marketplace.common.models

import ru.otus.otuskotlin.marketplace.common.models.product.IMkplAdProduct

data class MkplAd(
    var id: MkplAdId = MkplAdId.NONE,
    var title: String = "",
    var description: String = "",
    var ownerId: MkplUserId = MkplUserId.NONE,
    val adType: MkplDealSide = MkplDealSide.NONE,
    var visibility: MkplVisibility = MkplVisibility.NONE,
    var product: IMkplAdProduct = IMkplAdProduct.NONE,
    val permissionsClient: MutableSet<MkplAdPermissionClient> = mutableSetOf()
)
