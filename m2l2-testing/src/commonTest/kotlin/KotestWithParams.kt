import io.kotest.core.spec.style.FunSpec
import io.kotest.datatest.withData
import io.kotest.matchers.shouldBe

class KotestWithParams : FunSpec({
    context("Multiplication tests") {
        withData(
            mapOf(
                "10x2" to Triple(10, 2, 20),
                "20x2" to Triple(20, 2, 40),
                "30x2" to Triple(30, 2, 60),
            )
        ) { (a, b, c) ->
            a * b shouldBe c
        }
    }
})
