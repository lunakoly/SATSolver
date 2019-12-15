package sat.cdcl

import sat.constructor.Formula
import sat.intermediate.LeveledView

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
    open fun push(literal: Literal, wasDeduced: Boolean = false) {
        // prevent multiple exertions
        for (it in 0 until nextIndex) {
            if (values[it] == literal) {
                return
            }
        }

        if (!wasDeduced) {
            nextLevel += 1
        }

        values[nextIndex] = literal
        levels[nextIndex] = nextLevel

        nextIndex += 1
    }

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
            push(unassigned, true)
        }

        return AnalysisResult.OK
    }

    /**
     * Jumps to the beginning of the present
     * level, reverting all deductions made during it
     */
    open fun backtrack(): BacktrackingResult {
        val levelEndIndex = uncheckedIndex - 1
        val levelStartIndex = findLevelStart(levelEndIndex)

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
}