package common

import sat.general.AbstractClause
import sat.general.AbstractFormula
import sat.general.AbstractVariable
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

object LiteralTests {
    @Test
    fun getInversion(create: () -> AbstractVariable) {
        val a = create()
        val positiveA = +a
        val negativeA = -a

        assertNotEquals(positiveA.isPositive, negativeA.isPositive)
        assertNotEquals(positiveA, negativeA)

        assertEquals(positiveA.isPositive, negativeA.inversion.isPositive)
        assertEquals(positiveA, negativeA.inversion)

        assertEquals(negativeA.isPositive, positiveA.inversion.isPositive)
        assertEquals(negativeA, positiveA.inversion)
    }

    @Test
    fun plus(
        a: AbstractVariable,
        b: AbstractVariable,
        clause: AbstractClause
    ) {
        assertEquals(2, clause.literals.size)

        assertTrue(clause.literals.contains(+a))
        assertTrue(clause.literals.contains(+b))

        assertFalse(clause.literals.contains(-a))
        assertFalse(clause.literals.contains(-b))
    }

    @Test
    fun times(
        expression: AbstractFormula
    ) {
        assertEquals(2, expression.clauses.size)
    }

    @Test
    fun getVariable(create: () -> AbstractVariable) {
        val a = create()

        assertEquals((+a).variable, (-a).variable)
        assertEquals((+a).variable, (+a).inversion.variable)
        assertEquals((-a).variable, (-a).inversion.variable)
        assertEquals((+a).variable, (-a).inversion.variable)
        assertEquals((-a).variable, (+a).inversion.variable)
    }

    @Test
    fun isPositive(create: () -> AbstractVariable) {
        val a = create()

        assertEquals(+a, +a)
        assertEquals(-a, -a)
    }
}