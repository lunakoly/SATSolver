package cdcl

import constructor.Formula
import intermediate.components.LazyWatchlist

/**
 * Same as LearningView but can also skip
 * clauses that have already been approved
 */
class LazyLearningView(
    /**
     * An input formula constructed by
     * a user
     */
    formula: Formula
) : LearningView(formula) {
    override val watchlist = LazyWatchlist(cardinality, clauses)

    override fun deduce(): Pair<AnalysisResult, Clause?> {
        val literal = values[uncheckedIndex - 1]
        val approved = mutableSetOf<Clause>()

        for (clause in watchlist[literal]) {
            when (analyze(clause)) {
                AnalysisResult.UNSATISFIED -> return AnalysisResult.UNSATISFIED to clause
                AnalysisResult.APPROVED -> approved.add(clause)
                else -> { }
            }
        }

        for (clause in watchlist[literal.inversion]) {
            when (analyze(clause)) {
                AnalysisResult.UNSATISFIED -> return AnalysisResult.UNSATISFIED to clause
                AnalysisResult.APPROVED -> approved.add(clause)
                else -> { }
            }
        }

        for (it in approved) {
            watchlist.approve(it)
        }

        return AnalysisResult.OK to null
    }

    override fun backtrack(contradictingClause: Clause?): BacktrackingResult {
        val result = super.backtrack(contradictingClause)

        if (result == BacktrackingResult.OK) {
            watchlist.updateApproved {
                evaluate(it).third
            }
        }

        return result
    }
}