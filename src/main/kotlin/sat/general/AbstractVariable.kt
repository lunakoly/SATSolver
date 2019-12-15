package sat.general

/**
 * Represents an abstract variable.
 * A variable represents a superposition of
 * the possible states (true & false). To define
 * the exact value (turn a variable into a literal)
 * the one needs to apply unary plus or minus to it
 */
interface AbstractVariable {
    /**
     * Returns the literal representing
     * the variable positive state
     */
    operator fun unaryPlus(): AbstractLiteral

    /**
     * Returns the literal representing
     * the variable negative state
     */
    operator fun unaryMinus(): AbstractLiteral

    /**
     * Returns the literal representing
     * the isPositive state
     */
    fun toLiteral(isPositive: Boolean): AbstractLiteral
}