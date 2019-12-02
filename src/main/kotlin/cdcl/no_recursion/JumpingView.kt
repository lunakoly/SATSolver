package cdcl.no_recursion

import constructor.Formula
import intermediate.LeveledView

/**
 * A trivial implementation of LeveledView
 */
open class JumpingView(
    /**
     * An input formula constructed by
     * a user
     */
    formula: Formula
) : LeveledView(formula) {
    /**
     * Adds a literal to the list of possibly
     * approved ones that need to be checked later
     */
    fun push(literal: Literal, wasDeduced: Boolean = false) {
        if (!wasDeduced) {
            nextLevel += 1
        }

        values[nextIndex] = literal
        levels[nextIndex] = nextLevel

        nextIndex += 1
    }

    /**
     * Checks if a clause can be unsatisfied
     */
    override fun analyze(clause: Clause): AnalysisResult {
        val (unassignedLeft, unassigned, approved) = evaluate(clause)

        if (unassignedLeft == 0 && approved == 0) {
            return AnalysisResult.UNSATISFIED
        }

        // unassigned != null will always be true here
        // but let's satisfy at least the compiler
        else if (unassignedLeft == 1 && approved == 0 && unassigned != null) {
            push(unassigned, true)
        }

        return AnalysisResult.OK
    }
}