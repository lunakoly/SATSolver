package constructor

import general.AbstractLiteral

/**
 * Represents a particular state
 * of a variable that can be used to
 * construct a formula
 */
class Literal(
    override val variable: Variable,
    override val isPositive: Boolean
) : AbstractLiteral {
    override val inversion: Literal
        get() = Literal(variable, !isPositive)

    operator fun plus(other: Literal) = Clause(this, other)

    operator fun times(other: Literal) = Clause(this) * Clause(other)

    override fun equals(other: Any?): Boolean {
        if (
            other is Literal &&
            variable == other.variable &&
            isPositive == other.isPositive
        ) {
            return true
        }

        return false
    }

    override fun hashCode(): Int {
        var result = variable.hashCode()
        result = 31 * result + isPositive.hashCode()
        return result
    }
}