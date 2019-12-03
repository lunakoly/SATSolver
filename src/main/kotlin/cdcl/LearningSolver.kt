package cdcl

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
                    view.deduce().first == LeveledView.AnalysisResult.UNSATISFIED
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

            if (view.check() == LeveledView.CheckResult.DUPLICATE) {
                view.backtrack()
            } else {
                val (analysisResult, contradictingClause) = view.deduce()

                if (analysisResult == LeveledView.AnalysisResult.UNSATISFIED) {
                    view.learn(contradictingClause)
                    view.backtrack()
                }
            }
        }

        return view.exportSolution()
    }
}