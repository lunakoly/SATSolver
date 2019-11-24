package intermediate

import common.LiteralTests.getInversion
import common.LiteralTests.getVariable
import common.LiteralTests.isPositive
import common.VariableTests.testAll
import org.junit.jupiter.api.Test

internal class ShiftedViewTest {
    @Test
    fun testVariable() {
        var index = 0

        testAll {
            ShiftedView.Variable(index++)
        }
    }

    @Test
    fun testLiteral() {
        var index = 0

        getInversion {
            ShiftedView.Variable(index++)
        }

        getVariable {
            ShiftedView.Variable(index++)
        }

        isPositive {
            ShiftedView.Variable(index++)
        }
    }
}