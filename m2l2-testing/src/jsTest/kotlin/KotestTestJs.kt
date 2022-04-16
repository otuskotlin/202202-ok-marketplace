import io.kotest.matchers.shouldBe
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.string.shouldHaveLength

//class MyTests : StringSpec({
//    "length should return size of string" {
//        "hello".length shouldBe 5
//    }
//    "startsWith should test for a prefix" {
//        "world" should startWith("wor")
//    }
//})

class UUIDJsTest : FunSpec() {
    init {
        test("uuids should have length 21") {
            generateUUID().value.shouldHaveLength(21)
        }
    }
}
