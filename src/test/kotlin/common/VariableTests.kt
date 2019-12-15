package common

import sat.general.AbstractVariable
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Test

object VariableTests {
    @Test
    fun testEquality(create: () -> AbstractVariable) {
        val a = create()
        val b = create()
        assertEquals(a, a)
        assertNotEquals(a, b)
    }

    @Test
    fun testUnaryPlus(create: () -> AbstractVariable) {
        val a = create()
        val positiveA = +a
        assertEquals(a, positiveA.variable)
    }

    @Test
    fun testUnaryMinus(create: () -> AbstractVariable) {
        val a = create()
        val negativeA = -a
        assertEquals(a, negativeA.variable)
    }

    @Test
    fun testAll(create: () -> AbstractVariable) {
        testEquality(create)
        testUnaryPlus(create)
        testUnaryMinus(create)
    }
}