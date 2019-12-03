package cdcl

import common.SolverTests.testAll
import org.junit.jupiter.api.Test

internal class LearningSolverTest {
    @Test
    fun solve() {
        testAll {
            val view = LearningView(it)
            LearningSolver().solve(view)
        }

        testAll {
            val view = LazyLearningView(it)
            LearningSolver().solve(view)
        }
    }
}