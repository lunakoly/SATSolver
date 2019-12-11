package general

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
    fun represent(names: Map<out AbstractVariable, String>) = literals.joinToString(" * ") {
        if (it.isPositive) {
            names[it.variable] ?: "<undefined>"
        } else {
            "~" + (names[it.variable] ?: "<undefined>")
        }
    }
}