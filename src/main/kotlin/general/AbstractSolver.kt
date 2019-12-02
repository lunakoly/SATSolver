package general

import constructor.Literal

/**
 * Represents an abstract SAT solver.
 */
interface AbstractSolver<View : AbstractView> {
    /**
     * Returns the set of literals describing
     * the desired values for each variable or
     * null if no solution found
     */
    fun solve(view: View): Set<Literal>?
}