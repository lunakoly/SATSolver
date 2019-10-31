package dfs

import dfs
import general.Clause
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test

internal class DFSSolverTest {
    @Test
    fun solve() {
        dfs {
            val a = Variable("A")
            val b = Variable("B")
            val c = Variable("C")

            val expression = ((+a) + (-b) + (-c)) * ((+b) + (-c)) * Clause(+c)

            val solution = solve(expression)
            assertEquals(setOf(+a, +b, +c), solution)

//            println("Representation: " + represent(expression))
//            println("     // Source: $expression")
//            println("      Solution: " + represent(solution ?: emptySet()))
        }

        dfs {
            val a = Variable("A")

            val expression = Clause(+a) * (-a)

            val solution = solve(expression)
            assertNull(solution)
        }

        dfs {
            val a = Variable("A")
            val b = Variable("B")
            val c = Variable("C")

            val expression = Clause(+a) * (-b) * (+c)

            val solution = solve(expression)
            assertEquals(setOf(+a, -b, +c), solution)
        }
    }
}