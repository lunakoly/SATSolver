package cdcl

import constructor.Formula
import constructor.Solution

/**
 * LearningView with caching assignments
 * and pushed literals instead of calling
 * prepareAssignments() and iterating `values`
 * each time
 */
open class CachingLearningView(
    /**
     * An input formula constructed by
     * a user
     */
    formula: Formula
) : LearningView(formula) {
    /**
     * Assigned values of variables.
     */
    protected val assignments: Array<Boolean?> = Array(cardinality) { null }

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
    protected val pushes = mutableSetOf<Literal>()

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

    override fun push(literal: Literal, origin: Clause?) {
        // prevent multiple exertions
        if (!pushes.contains(literal)) {
            // to not have a deduction origin == to be selected manually
            if (origin == null) {
                nextLevel += 1
            }

            values[nextIndex] = literal
            levels[nextIndex] = nextLevel
            origins[nextIndex] = origin
            pushes.add(literal)

            nextIndex += 1
        }
    }

    override fun backtrack(contradictingClause: Clause?): BacktrackingResult {
        val levelEndIndex = uncheckedIndex - 1
        val levelStartIndex = findLevelStart(levelEndIndex)

        if (levels[levelStartIndex] == -1) {
            return BacktrackingResult.UNSATISFIED
        }

        // let's invert either the last manually assigned
        // variable (and all it's deductions) or the one
        // from the contradictingClause that was assigned
        // before the last. This is correct because in
        // contradictingClause the last assigned variable
        // is the only one left
        var backtrackingIndex = levelStartIndex

        // find a candidate
        if (contradictingClause != null) {
            var it = levelStartIndex - 1
            var shouldStop = false

            // find the latest literal from
            // the clause
            while (it >= 0 && !shouldStop) {
                for (literal in contradictingClause.literals) {
                    if (values[it] == literal) {
                        // find it's deduction level end
                        backtrackingIndex = findLevelEnd(it) + 1
                        shouldStop = true
                        break
                    }
                }

                it--
            }
        }

        pushes.remove(values[levelStartIndex])
        values[backtrackingIndex] = values[levelStartIndex].inversion
        assignments[values[backtrackingIndex].index] = null
        pushes.add(values[backtrackingIndex])

        if (backtrackingIndex == 0) {
            levels[backtrackingIndex] = -1
        } else {
            levels[backtrackingIndex] = levels[backtrackingIndex - 1]
        }

        // de-assign
        for (it in backtrackingIndex + 1 until nextIndex) {
            val literal = values[it]
            pushes.remove(literal)

            if (assignments[literal.index] == literal.isPositive) {
                assignments[literal.index] = null
            }
        }

        nextLevel = levels[backtrackingIndex]
        nextIndex = backtrackingIndex + 1

        // we want to check the inverted value
        // of the starting variable
        uncheckedIndex = backtrackingIndex

        return BacktrackingResult.OK
    }
}