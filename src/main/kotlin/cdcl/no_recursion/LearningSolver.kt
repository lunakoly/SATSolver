package cdcl.no_recursion

import constructor.Literal
import general.AbstractSolver
import intermediate.LeveledView

class LearningSolver : AbstractSolver<LearningView> {
    override fun solve(view: LearningView): Set<Literal>? {
        for (clause in view.clauses) {
            if (clause.literals.size == 1) {
                view.push(clause.literals.first(), clause)

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
                view.push(next, null)
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