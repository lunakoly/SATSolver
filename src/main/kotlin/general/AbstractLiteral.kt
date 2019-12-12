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

    /**
     * Allows to get human-readable
     * representation, e. g. `~A`
     */
    fun represent(name: String) = if (isPositive) name else "~$name"

    /**
     * Allows to get human-readable
     * representation, e. g. `~A`
     */
    fun represent(decode: (AbstractVariable) -> String) = represent(decode(variable))

    /**
     * Allows to get human-readable
     * representation, e. g. `~A`
     */
    fun represent(names: Map<out AbstractVariable, String>) = represent(names[variable] ?: "<unnamed>")
}