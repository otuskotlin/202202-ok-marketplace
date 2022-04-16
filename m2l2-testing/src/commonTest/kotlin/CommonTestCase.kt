import io.kotest.core.spec.style.FunSpec
import io.kotest.datatest.withData
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import io.kotest.matchers.string.shouldContainOnlyDigits

class UUIDTestCommon : FunSpec() {
    init {
        test("date should starts with year") {
            println(currentDate().iso)
            currentDate().iso
                .take(4)
                .shouldContainOnlyDigits()
        }

        test("date should contains separator") {
            currentDate().iso shouldContain "T"
        }

        test("date should conains date and time") {
            val data = currentDate().iso
                .split("T")

            //simple checs
            data.size shouldBe 2

            data.first().filter { it == '-' }.length shouldBe 2
            data.last().filter { it == ':' } shouldBe "--"
        }

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
    }
}
