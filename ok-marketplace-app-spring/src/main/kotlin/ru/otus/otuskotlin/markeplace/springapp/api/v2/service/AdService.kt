//package ru.otus.otuskotlin.markeplace.springapp.api.v2.service
//
//import marketplace.stubs.Bolt
//import org.springframework.stereotype.Service
//import ru.otus.otuskotlin.markeplace.springapp.api.v2.addError
//import ru.otus.otuskotlin.markeplace.springapp.api.v2.buildError
//import ru.otus.otuskotlin.marketplace.common.MkplContext
//
//@Service("adService2")
//class AdService {
//
//    fun createAd(mpContext: MkplContext): MkplContext {
//        return mpContext.apply {
//            adResponse = Bolt.getModel()
//        }
//    }
//
//    fun readAd(mpContext: MkplContext): MkplContext {
//        val requestedId = mpContext.requestId.asString()
//        val shouldReturnStub = Bolt.isCorrectId(requestedId)
//
//        return if (shouldReturnStub) {
//            mpContext.apply {
//                adResponse = Bolt.getModel()
//            }
//        } else {
//            mpContext.addError {
//                buildError().copy(field = "requestedId", message = "Not found ad by id $requestedId")
//            }
//        }
//    }
//
//    fun updateAd(context: MkplContext) = context.apply {
//        adResponse = adRequest
//    }
//
//
//    fun deleteAd(context: MkplContext): MkplContext {
//        val shouldReturnStub = Bolt.isCorrectId(context.requestId.asString())
//
//        return if (shouldReturnStub) {
//            context.apply {
//                adResponse = adRequest
//            }
//        } else {
//            context.addError {
//                buildError().copy(
//                    field = "id",
//                    message = "Ad with id ${context.requestId.asString()} doesn't exist"
//                )
//            }
//        }
//    }
//
//    fun findAd(context: MkplContext): MkplContext {
//        val filter = context.adFilterRequest
//
//        val idToFind = filter.ownerId
//
//        val shouldReturnStub = Bolt.isCorrectId(idToFind.asString())
//
//        return if (shouldReturnStub) {
//            context.apply {
//                adsResponse.addAll(
//                    Bolt.getModels()
//                )
//            }
//        } else {
//            context.addError {
//                buildError().copy(
//                    field = "id",
//                    message = "Ad with id ${idToFind.asString()} doesn't exist"
//                )
//            }
//        }
//    }
//}
//
