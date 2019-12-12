package cdcl

import common.SolverTests.testAll
import org.junit.jupiter.api.Test

internal class LearningSolverTest {
    @Test
    fun solve() {
        testAll {
            val view = LearningView(it)
            LearningSolver.solve(view)
        }
    }

    @Test
    fun solveLazy() {
        testAll {
            val view = LazyLearningView(it)
            LearningSolver.solve(view)
        }
    }

    @Test
    fun solveCaching() {
        testAll {
            val view = CachingLearningView(it)
            LearningSolver.solve(view)
        }
    }
}