package cdcl

import constructor.Formula
import intermediate.LeveledView

/**
 * LeveledView that learns
 * contradicting clauses
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
     * origin
     */
    fun learn(contradictingClause: Clause?) {
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
        }
    }
}