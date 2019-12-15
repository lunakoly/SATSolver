package sat.general

/**
 * Some structure for representing solutions
 */
interface AbstractSolution {
    /**
     * Assignments of variables that satisfy
     * formula
     */
    val literals: Set<AbstractLiteral>

    /**
     * Allows to get human-readable
     * representation, e. g. `A * ~B * ~C`
     */
    fun represent(decode: (AbstractVariable) -> String)
            = literals.joinToString(" * ") { it.represent(decode) }

    /**
     * Allows to get human-readable
     * representation, e. g. `A * ~B * ~C`
     */
    fun represent(names: Map<out AbstractVariable, String>)
            = literals.joinToString(" * ") { it.represent(names) }
}