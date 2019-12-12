package general

/**
 * Represents an abstract SAT solver.
 * Solver is an attempt to separate an algorithm
 * from underlying implementation aspects to
 * make the code a bit more readable.
 * View is the solver minimum implementation
 * requirements declaration
 */
interface AbstractSolver<View : AbstractView> {
    /**
     * Returns the set of literals (a Solution) describing
     * the desired values for each variable or
     * null if no solution found
     */
    fun solve(view: View): AbstractSolution?
}