package general

/**
 * Represents a particular state
 * of a variable
 */
interface Literal {
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
    val inversion: Literal
}