package sat.cdcl

import sat.constructor.Solution
import sat.general.AbstractSolver
import sat.intermediate.LeveledView

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
                if (view.backtrack(null) == LeveledView.BacktrackingResult.UNSATISFIED) {
                    return null
                }
            } else {
                val (analysisResult, contradictingClause) = view.deduce()

                if (analysisResult == LeveledView.AnalysisResult.UNSATISFIED) {
                    val learnedClause = view.learn(contradictingClause)

                    if (view.backtrack(learnedClause) == LeveledView.BacktrackingResult.UNSATISFIED) {
                        return null
                    }
                }
            }
        }

        return view.exportSolution()
    }
}