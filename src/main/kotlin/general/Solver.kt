package general

/**
 * Represents an abstract SAT solver.
 * It should take care of variable indexing,
 * and allocating necessary context data
 */
@Suppress("FunctionName")
interface Solver {
    /**
     * A sugar shortcut for variable definition.
     * Introduces automatic indexing
     */
    fun Variable(name: String): Variable
}