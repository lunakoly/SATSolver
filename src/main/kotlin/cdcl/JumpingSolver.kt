package cdcl

import constructor.Literal
import general.AbstractSolver
import intermediate.LeveledView

/**
 * A solver that can do non-chronological jumps
 */
class JumpingSolver : AbstractSolver<JumpingView> {
    override fun solve(view: JumpingView): Set<Literal>? {
        for (clause in view.clauses) {
            if (clause.literals.size == 1) {
                view.push(clause.literals.first(), true)

                if (
                    view.check() == LeveledView.CheckResult.DUPLICATE ||
                    view.deduce() == LeveledView.AnalysisResult.UNSATISFIED
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
                view.deduce() == LeveledView.AnalysisResult.UNSATISFIED
            ) {
                view.backtrack()
            }
        }

        return view.exportSolution()
    }
}