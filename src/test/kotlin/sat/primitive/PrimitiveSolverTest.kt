package sat.primitive

import common.SolverTests.testAll
import org.junit.jupiter.api.Test

internal class PrimitiveSolverTest {
    @Test
    fun solve() {
        testAll {
            val view = PrimitiveView(it)
            PrimitiveSolver.solve(view)
        }
    }
}