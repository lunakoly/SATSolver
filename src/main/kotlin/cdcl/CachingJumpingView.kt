package cdcl

import constructor.Formula
import constructor.Solution

/**
 * JumpingView with caching assignments
 * and pushed literals instead of calling
 * prepareAssignments() and iterating `values`
 * each time
 */
open class CachingJumpingView(
    /**
     * An input formula constructed by
     * a user
     */
    formula: Formula
) : JumpingView(formula) {
    /**
     * Assigned values of variables.
     */
    private val assignments: Array<Boolean?> = Array(cardinality) { null }

    override fun exportSolution(): Solution {
        val assignments = assignments
            .mapIndexed { index, isPositive ->
                Variable(index).toOuter().toLiteral(isPositive ?: false)
            }
            .toSet()

        return Solution(assignments)
    }

    /**
     * Speeds up checking if a literal has
     * already been pushed
     */
    private val pushes = mutableSetOf<Literal>()

    override fun check(): CheckResult {
        val literal = values[uncheckedIndex]

        if (assignments[literal.index] == literal.isPositive) {
            // due to the algorithm it can only happen
            // if literal.isPositive != other.isPositive
            // so we need to backtrack.
            // DOUBLE INSERTIONS ARE PREVENTED
            // INSIDE PUSH()
            return CheckResult.DUPLICATE
        }

        uncheckedIndex += 1
        assignments[literal.index] = literal.isPositive
        return CheckResult.OK
    }

    override fun evaluate(clause: Clause): Triple<Int, Literal?, Int> {
        var someUnassigned: Literal? = null
        var unassignedLeft = 0
        var approved = 0

        for (literal in clause.literals) {
            if (assignments[literal.index] == literal.isPositive) {
                approved += 1
            } else if (assignments[literal.index] == null) {
                someUnassigned = literal
                unassignedLeft += 1
            }
        }

        return Triple(unassignedLeft, someUnassigned, approved)
    }

    override fun getNextLiteral(): Literal {
        for (it in 0 until cardinality) {
            val variable = Variable(it)

            if (assignments[variable.index] == null) {
                return +variable
            }
        }

        throw IllegalStateException("No next elements")
    }

    override fun push(literal: Literal, wasDeduced: Boolean) {
        // prevent multiple insertions
        if (!pushes.contains(literal)) {
            if (!wasDeduced) {
                nextLevel += 1
            }

            values[nextIndex] = literal
            levels[nextIndex] = nextLevel
            pushes.add(literal)

            nextIndex += 1
        }
    }

    /**
     * Jumps to the beginning of the present
     * level, reverting all deductions made during it
     */
    override fun backtrack(): BacktrackingResult {
        val levelEndIndex = uncheckedIndex - 1
        val levelStartIndex = findLevelStart(levelEndIndex)

        if (levels[levelStartIndex] == -1) {
            return BacktrackingResult.UNSATISFIED
        }

        pushes.remove(values[levelStartIndex])
        values[levelStartIndex] = values[levelStartIndex].inversion
        assignments[values[levelStartIndex].index] = null
        pushes.add(values[levelStartIndex])

        levels[levelStartIndex] -= 1
        nextLevel = levels[levelStartIndex]

        // de-assign
        for (it in levelStartIndex + 1 until nextIndex) {
            val literal = values[it]
            pushes.remove(literal)

            if (assignments[literal.index] == literal.isPositive) {
                assignments[literal.index] = null
            }
        }

        nextIndex = levelStartIndex + 1

        // we want to check the inverted value
        // of the starting variable
        uncheckedIndex = levelStartIndex

        return BacktrackingResult.OK
    }
}