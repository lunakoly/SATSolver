package general

/**
 * Represents a conjunction of abstract clauses.
 */
interface AbstractFormula {
    /**
     * Allows to access the clauses
     */
    val clauses: Set<AbstractClause>

    /**
     * Returns true if `solution` satisfies
     * the formula
     */
    fun satisfies(solution: Set<AbstractLiteral>): Boolean

    /**
     * Allows to get human-readable
     * representation, e. g. `(A + ~B + ~C)`
     */
    fun represent(decode: (AbstractVariable) -> String)
            = clauses.joinToString(" * ") { it.represent(decode) }

    /**
     * Allows to get human-readable
     * representation, e. g. `(A + ~B + ~C)`
     */
    fun represent(names: Map<AbstractVariable, String>) = clauses.joinToString(" * ") {
        it.represent { variable ->
            names[variable] ?: "<unnamed>"
        }
    }
}