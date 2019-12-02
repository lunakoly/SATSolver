package common

import constructor.Formula
import constructor.Literal
import constructor.Variable
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

object SolverTests {
    @Test
    fun testA(solve: (Formula) -> Set<Literal>?) {
        val a = Variable()
        val b = Variable()
        val c = Variable()

        val expression = (+a + -b + -c) * (+b + -c) * +c
        val solution = solve(expression)

        assertEquals(setOf(+a, +b, +c), solution)
    }

    @Test
    fun testB(solve: (Formula) -> Set<Literal>?) {
        val a = Variable()

        val expression = +a * -a
        val solution = solve(expression)

        assertNull(solution)
    }

    @Test
    fun testC(solve: (Formula) -> Set<Literal>?) {
        val a = Variable()
        val b = Variable()
        val c = Variable()

        val expression = +a * -b * +c
        val solution = solve(expression)

        assertEquals(setOf(+a, -b, +c), solution)
    }

    @Test
    fun testD(solve: (Formula) -> Set<Literal>?) {
        val a = Variable()
        val b = Variable()
        val c = Variable()

        val expression = (+a + +b) * (-b + +c + -a) * (-c + -b) * (+c + -b + +a)
        val solution = solve(expression)

        assertNotNull(solution)
        assertTrue(expression.satisfies(solution!!))
    }

    @Test
    fun testAll(solve: (Formula) -> Set<Literal>?) {
        testA(solve)
        testB(solve)
        testC(solve)
        testD(solve)
    }
}