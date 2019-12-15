package sat.constructor

import sat.general.AbstractSolution

/**
 * Represents a solution.
 * that can be used later as an input for a solver
 */
class Solution(
    /**
     * Assignments of variables that
     * satisfy the formula
     */
    override val literals: Set<Literal>
) : AbstractSolution {
    override fun equals(other: Any?): Boolean {
        if (other !is Solution) {
            return false
        }

        return literals == other.literals
    }

    override fun hashCode(): Int {
        return literals.hashCode()
    }
}