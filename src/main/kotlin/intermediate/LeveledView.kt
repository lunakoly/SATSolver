package intermediate

import constructor.Formula
import constructor.Solution
import intermediate.components.ShiftedWatchlist

/**
 * A view that maps each assigned
 * literal to the specific level of
 * deduction and performs backtracking
 * jumps over the whole levels and not
 * just literals
 */
abstract class LeveledView(
    /**
     * An input formula constructed by
     * a user
     */
    formula: Formula
) : ShiftedView(formula) {
    /**
     * Associates a literal and a set of clauses
     * it is met in
     */
    protected open val watchlist = ShiftedWatchlist(cardinality, clauses)

    /**
     * Assigned values of variables.
     * `* 2` helps with one extra assignment backtracking
     */
    protected val values: Array<Literal> = Array(cardinality * 2) { Literal(0) }

    /**
     * Returns the set of literals that
     * satisfy the formula if called at the
     * right time
     */
    fun exportSolution(): Solution {
        val assignments = values
            .slice(0 until cardinality)
            .map { it.toOuter() }
            .toSet()

        return Solution(assignments)
    }

    /**
     * Index of the cell we should
     * backtrack to. Deduced literals have
     * the same level
     */
    protected val levels: Array<Int> = Array(cardinality * 2) { -1 }

    /**
     * Points to the next empty cell
     */
    protected var nextIndex = 0

    /**
     * Points the the next unchecked cell
     */
    protected var uncheckedIndex = 0

    /**
     * Returns true if the solution is found
     */
    fun isSolutionFound() = uncheckedIndex == cardinality

    /**
     * Level assigned to all next literals
     * pushed with an origin
     */
    protected var nextLevel = -1

    /**
     * Returns true if there're some literals
     * worth checking
     */
    fun hasUnchecked() = uncheckedIndex < nextIndex

    /**
     * Represents the situation occurred while
     * checking the literal `to-check` list (= values).
     * DUPLICATE means that the first unchecked variable
     * has already been assigned a value (checked before)
     * and this may only happen in case of an attempt of
     * an opposite assignment
     */
    enum class CheckResult {
        DUPLICATE, OK
    }

    /**
     * Tests if the next unchecked literal is
     * a duplicate assignment of any previously defined
     * variable
     */
    fun check(): CheckResult {
        val literal = values[uncheckedIndex]

        for (it in uncheckedIndex - 1 downTo 0) {
            val other = values[it]
            // same variable
            if (other.index == literal.index) {
                // due to the algorithm it can only happen
                // if literal.isPositive != other.isPositive
                // so we need to backtrack.
                // DOUBLE INSERTIONS ARE PREVENTED
                // INSIDE PUSH()
                return CheckResult.DUPLICATE
            }
        }

        uncheckedIndex += 1
        return CheckResult.OK
    }

    /**
     * Represents the result of backtracking.
     * UNSATISFIED means that we formula is
     * unsatisfiable at all. Otherwise OK
     */
    enum class BacktrackingResult {
        UNSATISFIED, OK
    }

    /**
     * Jumps to the beginning of the present
     * level, reverting all deductions made during it
     */
    open fun backtrack(): BacktrackingResult {
        val levelEndIndex = uncheckedIndex - 1
        var levelStartIndex = levelEndIndex

        for (it in 0 until uncheckedIndex - 1) {
            if (levels[it] == levels[uncheckedIndex - 1]) {
                levelStartIndex = it
                break
            }
        }

        if (levels[levelStartIndex] == -1) {
            return BacktrackingResult.UNSATISFIED
        }

        values[levelStartIndex] = values[levelStartIndex].inversion

        levels[levelStartIndex] -= 1
        nextLevel = levels[levelStartIndex]

        nextIndex = levelStartIndex + 1
        // we want to check the inverted value
        // of the starting variable
        uncheckedIndex = levelStartIndex

        return BacktrackingResult.OK
    }

    /**
     * Returns a map from variables into their values.
     * It helps to check whether a variable has been assigned
     * a value or not
     */
    private fun prepareAssignments(): MutableMap<Variable, Boolean> {
        val assignments = mutableMapOf<Variable, Boolean>()

        for (it in 0 until uncheckedIndex) {
            assignments[values[it].variable] = values[it].isPositive
        }

        return assignments
    }

    /**
     * Returns info about a clause (unassignedLeft, someUnassigned, approved)
     */
    protected fun evaluate(clause: Clause): Triple<Int, Literal?, Int> {
        val assignments = prepareAssignments()

        var someUnassigned: Literal? = null
        var unassignedLeft = 0
        var approved = 0

        for (literal in clause.literals) {
            if (assignments[literal.variable] == literal.isPositive) {
                approved += 1
            } else if (assignments[literal.variable] == null) {
                someUnassigned = literal
                unassignedLeft += 1
            }
        }

        return Triple(unassignedLeft, someUnassigned, approved)
    }

    /**
     * Represents a state of algorithm.
     * UNSATISFIED means that some clause can't be
     * satisfied with the present assignments.
     * APPROVED means that the clause is already
     * satisfied. Otherwise OK
     */
    enum class AnalysisResult {
        UNSATISFIED, OK, APPROVED
    }

    /**
     * Checks if a clause can be unsatisfied
     */
    protected abstract fun analyze(clause: Clause): AnalysisResult

    /**
     * Based on the last checked index, checks if all
     * clauses it's met in can be unsatisfied with
     * the present assignments
     */
    open fun deduce(): Pair<AnalysisResult, Clause?> {
        val literal = values[uncheckedIndex - 1]

        for (clause in watchlist[literal]) {
            if (analyze(clause) == AnalysisResult.UNSATISFIED) {
                return AnalysisResult.UNSATISFIED to clause
            }
        }

        for (clause in watchlist[literal.inversion]) {
            if (analyze(clause) == AnalysisResult.UNSATISFIED) {
                return AnalysisResult.UNSATISFIED to clause
            }
        }

        return AnalysisResult.OK to null
    }

    /**
     * Returns the next literal the solver needs to check
     */
    fun getNextLiteral(): Literal {
        val assignments = prepareAssignments()

        for (it in 0 until cardinality) {
            val variable = Variable(it)

            if (assignments[variable] == null) {
                return +variable
            }
        }

        throw IllegalStateException("No next elements")
    }
}