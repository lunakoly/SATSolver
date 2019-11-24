package general

/**
 * Represents a disjunction of abstract literals.
 */
interface AbstractClause {
    /**
     * Allows to access the literals
     */
    val literals: Set<AbstractLiteral>

    /**
     * Allows to get human-readable
     * representation, e. g. `(A + ~B + ~C)`
     */
    fun represent(decode: (AbstractVariable) -> String)
            = "(" + literals.joinToString(" + ") { decode(it.variable) } + ")"
}