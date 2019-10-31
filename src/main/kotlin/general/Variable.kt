package general

/**
 * Represents a variable. A variable is
 * defined via some unique integer value.
 * A variable represents a superposition of
 * the possible states (true & false). To define
 * the exact value (turn a variable into a literal)
 * the one needs to apply unary plus or minus to it
 */
interface Variable {
    /**
     * Returns the literal representing
     * the variable positive state
     */
    operator fun unaryPlus(): Literal

    /**
     * Returns the literal representing
     * the variable negative state
     */
    operator fun unaryMinus(): Literal
}