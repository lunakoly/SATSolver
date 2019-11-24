package constructor

import common.LiteralTests.getInversion
import common.LiteralTests.getVariable
import common.LiteralTests.isPositive
import common.LiteralTests.plus
import common.LiteralTests.times
import org.junit.jupiter.api.Test

internal class LiteralTest {
    @Test
    fun test() {
        getInversion {
            Variable()
        }

        getVariable {
            Variable()
        }

        isPositive {
            Variable()
        }

        val a = Variable()
        val b = Variable()

        plus(a, b, +a + +b)
        times(+a * +b)
    }
}