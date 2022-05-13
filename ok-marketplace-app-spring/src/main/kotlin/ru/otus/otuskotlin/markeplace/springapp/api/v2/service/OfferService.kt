//package ru.otus.otuskotlin.markeplace.springapp.api.v2.service
//
//import marketplace.stubs.Bolt
//import org.springframework.stereotype.Service
//import ru.otus.otuskotlin.marketplace.common.MkplContext
//import ru.otus.otuskotlin.marketplace.common.models.MkplUserId
//
//@Service("offerService2")
//class OfferService {
//
//    fun readOffers(context: MkplContext): MkplContext {
//        val ownerId = context.adFilterRequest.ownerId
//        val shouldReturnSingle = ownerId != MkplUserId.NONE
//
//        return if (shouldReturnSingle) {
//            context.apply {
//                adsResponse.add(Bolt.getModel())
//            }
//        } else {
//            context.apply {
//                adsResponse.addAll(Bolt.getModels())
//                adFilterRequest = context.adFilterRequest
//            }
//        }
//    }
//}