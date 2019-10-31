package shifted

import general.Clause
import general.Literal

/**
 * Defines a state of a variable with
 * index `value shr 1`.
 *      positive -> value is even
 *      negative -> value is odd
 */
class ShiftedLiteral(val value: Int) : Literal {
    /**
     * Returns the index of the variable
     * the literal describes the state of
     */
    val index: Int
        get() = value shr 1

    /**
     * Returns the value of the literal
     * representing the variables
     * negative state
     */
    val invertedValue: Int
        get() = value xor 1

    /**
     * Returns true if the literal represents
     * a positive variable state
     */
    override val isPositive: Boolean
        get() = value and 1 == 0

    /**
     * Returns the literal holding
     * the inverted value of the same
     * variable
     */
    override val inversion: ShiftedLiteral
        get() = ShiftedLiteral(invertedValue)

    /**
     * Wraps up the two literals into
     * a disjunction (a clause)
     */
    operator fun plus(other: ShiftedLiteral) = Clause(this, other)

    /**
     * Either `N` or `~N`
     */
    override fun toString() = if (isPositive) {
        index.toString()
    } else {
        "~$index"
    }

    /**
     * Otherwise comparing collections of
     * literals becomes impossible
     */
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ShiftedLiteral

        return value == other.value
    }

    /**
     * Auto-generated
     */
    override fun hashCode(): Int {
        return value
    }
}