package cdcl

import constructor.Solution
import general.AbstractSolver
import intermediate.LeveledView

/**
 * Solver that can both do non-chronological
 * jumps and learn contradicting clauses
 */
object LearningSolver : AbstractSolver<LearningView> {
    override fun solve(view: LearningView): Solution? {
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

                    if (view.backtrack() == LeveledView.BacktrackingResult.UNSATISFIED) {
                        return null
                    }
                }
            }
        }

        return view.exportSolution()
    }
}