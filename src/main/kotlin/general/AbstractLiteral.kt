package general

/**
 * Represents a particular state
 * of an abstract variable
 */
interface AbstractLiteral {
    /**
     * Returns the underlying variable
     * the literal represents
     */
    val variable: AbstractVariable

    /**
     * Returns true if the literal represents
     * a positive variable state
     */
    val isPositive: Boolean

    /**
     * Returns the literal holding
     * the inverted value of the same
     * variable
     */
    val inversion: AbstractLiteral
}