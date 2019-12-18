package common

import sat.constructor.Formula
import sat.constructor.Solution
import sat.constructor.Variable
import sat.loaders.DIMACSLoader
import org.junit.jupiter.api.Assertions.*
import java.io.File

object SolverTests {
    private fun testA(solve: (Formula) -> Solution?) {
        val a = Variable()
        val b = Variable()
        val c = Variable()

        val expression = (+a + -b + -c) * (+b + -c) * +c
        val solution = solve(expression)

        assertEquals(Solution(setOf(+a, +b, +c)), solution)
    }

    private fun testB(solve: (Formula) -> Solution?) {
        val a = Variable()

        val expression = +a * -a
        val solution = solve(expression)

        assertNull(solution)
    }

    private fun testC(solve: (Formula) -> Solution?) {
        val a = Variable()
        val b = Variable()
        val c = Variable()

        val expression = +a * -b * +c
        val solution = solve(expression)

        assertEquals(Solution(setOf(+a, -b, +c)), solution)
    }

    private fun testD(solve: (Formula) -> Solution?) {
        val a = Variable()
        val b = Variable()
        val c = Variable()

        val expression = (+a + +b) * (-b + +c + -a) * (-c + -b) * (+c + -b + +a)
        val solution = solve(expression)

        assertNotNull(solution)
        assertTrue(expression.satisfies(solution!!))
    }

    private fun testE(solve: (Formula) -> Solution?) {
        val x = Array(12) { Variable() }

        val expression =
                (+x[0] + +x[3]) *
                (+x[0] + -x[2] + -x[7]) *
                (+x[0] + +x[7] + +x[11]) *
                (+x[1] + +x[10]) *
                (-x[6] + -x[2] + +x[8]) *
                (-x[6] + +x[7] + -x[8]) *
                (+x[6] + +x[7] + -x[9]) *
                (+x[6] + +x[9] + -x[11])
        val solution = solve(expression)

        assertNotNull(solution)
        assertTrue(expression.satisfies(solution!!))
    }

    private fun testF(solve: (Formula) -> Solution?) {
        val x1 = Variable()
        val x2 = Variable()
        val x3 = Variable()
        val x4 = Variable()

        val expression =
                (-x1 + +x2 + +x3) *
                (-x2 + -x3 + +x4) *
                (+x3 + +x1 + +x4)
        val solution = solve(expression)

        assertNotNull(solution)
        assertTrue(expression.satisfies(solution!!))
    }

    private fun testG(solve: (Formula) -> Solution?) {
        val a = Variable()
        val b = Variable()
        val c = Variable()

        val expression =
                (-a + +b) *
                (-b + -c) *
                (+c + -a)
        val solution = solve(expression)

        assertNotNull(solution)
        assertTrue(expression.satisfies(solution!!))
    }

    private fun benchmarkFile(filename: String, solve: (Formula) -> Solution?) {
        val problem = DIMACSLoader.load(File(filename))

        val expression = problem.first
        val solution = solve(expression)

        assertNotNull(solution)
        assertTrue(expression.satisfies(solution!!))
    }

    private fun benchmark(solve: (Formula) -> Solution?) {
        for (i in 1..1000) {
            benchmarkFile("benchmark/uf20-91/uf20-0$i.cnf", solve)
        }
    }

    fun testAll(solve: (Formula) -> Solution?) {
        testA(solve)
        testB(solve)
        testC(solve)
        testD(solve)
        testE(solve)
        testF(solve)
        testG(solve)
        benchmark(solve)
    }
}