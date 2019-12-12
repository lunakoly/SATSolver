package cdcl

import constructor.Formula
import intermediate.LeveledView

/**
 * LeveledView that learns
 * contradicting clauses and can do NCB
 */
open class LearningView(
    /**
     * An input formula constructed by
     * a user
     */
    formula: Formula
) : LeveledView(formula) {
    /**
     * Maps an assigned value to the
     * literal it's been deduced from
     */
    private val origins: Array<Clause?> = Array(cardinality * 2) { null }

    /**
     * Adds a literal to the list of possibly
     * approved ones that need to be checked later
     */
    fun push(literal: Literal, origin: Clause? = null) {
        // prevent multiple exertions
        for (it in 0 until nextIndex) {
            if (values[it] == literal) {
                return
            }
        }

        // to not have a deduction origin == to be selected manually
        if (origin == null) {
            nextLevel += 1
        }

        values[nextIndex] = literal
        levels[nextIndex] = nextLevel
        origins[nextIndex] = origin

        nextIndex += 1
    }

    /**
     * Checks if a clause can be unsatisfied
     */
    override fun analyze(clause: Clause): AnalysisResult {
        val (unassignedLeft, unassigned, approved) = evaluate(clause)

        if (approved > 0) {
            return AnalysisResult.APPROVED
        }

        if (unassignedLeft == 0 && approved == 0) {
            return AnalysisResult.UNSATISFIED
        }

        // unassigned != null will always be true here
        // but let's satisfy at least the compiler
        else if (unassignedLeft == 1 && approved == 0 && unassigned != null) {
            push(unassigned, clause)
        }

        return AnalysisResult.OK
    }

    /**
     * Assuming that the last checked literal
     * controverts the given clause, learns the common
     * variables from `contradictingClause` and the literal
     * origin. Returns the new clause or null
     */
    fun learn(contradictingClause: Clause?): Clause? {
        val contradictingVariable = values[uncheckedIndex - 1].variable
        val literalOrigin = origins[uncheckedIndex - 1]

        if (
            contradictingClause != null &&
            literalOrigin != null
        ) {
            // no duplicates because Clause
            // stores a set of literals
            val result = Clause()

            contradictingClause.literals
                .filter { it.variable == contradictingVariable }
                .forEach {
                    result.literals.add(it)
                }

            literalOrigin.literals
                .filter { it.variable == contradictingVariable }
                .forEach {
                    result.literals.add(it)
                }

            clauses.add(result)
            return result
        }

        return null
    }

    /**
     * Non-chronological jumps over
     * previous levels
     */
    open fun backtrack(contradictingClause: Clause?): BacktrackingResult {
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

        values[backtrackingIndex] = values[levelStartIndex].inversion

        if (backtrackingIndex == 0) {
            levels[backtrackingIndex] = -1
        } else {
            levels[backtrackingIndex] = levels[backtrackingIndex - 1]
        }

        nextLevel = levels[backtrackingIndex]
        nextIndex = backtrackingIndex + 1

        // we want to check the inverted value
        // of the starting variable
        uncheckedIndex = backtrackingIndex

        return BacktrackingResult.OK
    }
}