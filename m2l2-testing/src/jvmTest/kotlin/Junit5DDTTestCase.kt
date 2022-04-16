import org.junit.jupiter.api.*
import org.junit.jupiter.api.function.Executable
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.assertEquals


internal class Junit5DDTTestCase {

    @TestFactory
    fun `test multi`() = listOf(
        DynamicTest.dynamicTest("when I multiply 10*2 then I get 20") {
            Assertions.assertEquals(20, 10 * 2)
        },
        DynamicTest.dynamicTest("when I multiply 10*0 then I get 0") {
            Assertions.assertEquals(0, 10 * 0)
        },
    )

    private val data = listOf(
        1 to 13 to 13,
        2 to 21 to 42,
        3 to 34 to 102,
        4 to 55 to 220,
        5 to 89 to 445,
    )

    @TestFactory
    fun testSquares() = data.map { (nums, expected) ->
        DynamicTest.dynamicTest("when I multiply ${nums.first}*${nums.second} then I get $expected") {
            Assertions.assertEquals(expected, nums.first * nums.second)
        }
    }
}
