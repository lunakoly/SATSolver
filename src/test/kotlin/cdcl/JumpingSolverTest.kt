package cdcl

import common.SolverTests.testAll
import org.junit.jupiter.api.Test

internal class JumpingSolverTest {
    @Test
    fun solve() {
        testAll {
            val view = JumpingView(it)
            JumpingSolver.solve(view)
        }
    }
}