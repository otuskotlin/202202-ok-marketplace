import ru.otus.otuskotlin.marketplace.api.logs.mapper.toLog
import ru.otus.otuskotlin.marketplace.common.MkplContext
import ru.otus.otuskotlin.marketplace.common.models.*
import kotlin.test.Test

class MapperTest {

    @Test
    fun fromContext() {
        val context = MkplContext(
            requestId = MkplRequestId("1234"),
            command = MkplCommand.CREATE,
            adResponse = MkplAd(
                title = "title",
                description = "desc",
                adType = MkplDealSide.DEMAND,
                visibility = MkplVisibility.VISIBLE_PUBLIC,
            ),
            errors = mutableListOf(
                MkplError(
                    code = "err",
                    group = "request",
                    field = "title",
                    message = "wrong title",
                )
            ),
            state = MkplState.RUNNING,
        )

        val log = context.toLog("test-id")

        println(log)
    }
}
