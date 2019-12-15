package sat.cdcl

import sat.constructor.Solution
import sat.general.AbstractSolver
import sat.intermediate.LeveledView

/**
 * A solver that can do jumps over deduction levels
 */
object JumpingSolver : AbstractSolver<JumpingView> {
    override fun solve(view: JumpingView): Solution? {
        for (clause in view.clauses) {
            if (clause.literals.size == 1) {
                view.push(clause.literals.first(), true)

                if (
                    view.check() == LeveledView.CheckResult.DUPLICATE ||
                    view.deduce().first == LeveledView.AnalysisResult.UNSATISFIED
                ) {
                    return null
                }
            }
        }

        while (!view.isSolutionFound()) {
            if (!view.hasUnchecked()) {
                val next = view.getNextLiteral()
                view.push(next, false)
            }

            if (
                view.check() == LeveledView.CheckResult.DUPLICATE ||
                view.deduce().first == LeveledView.AnalysisResult.UNSATISFIED
            ) {
                if (view.backtrack() == LeveledView.BacktrackingResult.UNSATISFIED) {
                    return null
                }
            }
        }

        return view.exportSolution()
    }
}