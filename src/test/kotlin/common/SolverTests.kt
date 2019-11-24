package common

import constructor.Formula
import constructor.Literal
import constructor.Variable
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
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

//        println("Representation: " + expression.represent())
//        println("      Solution: " + represent(solution ?: emptySet()))
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
    fun testAll(solve: (Formula) -> Set<Literal>?) {
        testA(solve)
        testB(solve)
        testC(solve)
    }
}