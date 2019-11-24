package cdcl

import common.SolverTests.testAll
import org.junit.jupiter.api.Test

internal class CDCLSolverTest {
    @Test
    fun solve() {
        testAll {
            val view = CDCLView(it)
            CDCLSolver().solve(view)
        }
    }
}