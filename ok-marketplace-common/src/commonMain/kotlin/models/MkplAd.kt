package ru.otus.otuskotlin.marketplace.common.models

import ru.otus.otuskotlin.marketplace.common.models.product.IMkplAdProduct

data class MkplAd(
    var id: MkplAdId = MkplAdId.NONE,
    var title: String = "",
    var description: String = "",
    var ownerId: MkplUserId = MkplUserId.NONE,
    var adType: MkplDealSide = MkplDealSide.NONE,
    var visibility: MkplVisibility = MkplVisibility.NONE,
    var product: IMkplAdProduct = IMkplAdProduct.NONE,
    var lock: MkplAdLock = MkplAdLock.NONE,
    val permissionsClient: MutableSet<MkplAdPermissionClient> = mutableSetOf(),
) {
    fun deepCopy(
    ) = MkplAd(
        id = this@MkplAd.id,
        title = this@MkplAd.title,
        description = this@MkplAd.description,
        ownerId = this@MkplAd.ownerId,
        adType = this@MkplAd.adType,
        visibility = this@MkplAd.visibility,
        lock = this@MkplAd.lock,
        product = this@MkplAd.product.deepCopy(),
        permissionsClient = this@MkplAd.permissionsClient.toMutableSet()
    )
}
