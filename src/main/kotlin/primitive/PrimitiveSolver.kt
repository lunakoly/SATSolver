package primitive

import constructor.Solution
import general.AbstractSolver
import intermediate.ShiftedView

/**
 * A solver that utilizes a dfs and
 * performs contradiction detection
 */
@Suppress("FunctionName")
object PrimitiveSolver : AbstractSolver<PrimitiveView> {
    /**
     * Returns true if a contradiction is detected.
     * Contradiction detection is done via checking if
     * there is a clause with all variables assigned false
     */
    private fun contradicts(next: ShiftedView.Literal, view: PrimitiveView): Boolean {
        for (clause in view.getRelativeClauses(next)) {
            var canHaveNonFalseLiteral = false

            for (literal in clause.literals) {
                if (
                    view.haveAlreadyChecked(literal) ||
                    literal.isPositive == view.getValueOf(literal.variable)
                ) {
                    canHaveNonFalseLiteral = true
                    break
                }
            }

            if (!canHaveNonFalseLiteral) {
                return true
            }
        }

        return false
    }

    override fun solve(view: PrimitiveView): Solution? {
        while (view.hasNextLiteral()) {
            val next = view.getNextLiteral()

            // try assign one more value
            view.setValueOf(next.variable, next.isPositive)
            view.check(next)

            when {
                contradicts(next, view) ->  {
                    view.backtrack(next)
                }

                view.haveCheckedEverything() -> {
                    return view.exportSolution()
                }

                else -> {
                    view.pickNextLiterals()
                }
            }
        }

        return null
    }
}