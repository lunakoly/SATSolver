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

    override fun backtrack() {
        val levelEndIndex = uncheckedIndex - 1
        var levelStartIndex = levelEndIndex

        for (it in 0 until uncheckedIndex - 1) {
            if (levels[it] == levels[uncheckedIndex - 1]) {
                levelStartIndex = it
                break
            }
        }

        values[levelStartIndex] = values[levelStartIndex].inversion

        if (levelStartIndex == 0) {
            levels[levelStartIndex] -= 1
        } else {
            levels[levelStartIndex] = levels[levelStartIndex - 1]
        }

        watchlist.updateApproved {
            evaluate(it).third
        }

        nextIndex = levelStartIndex + 1
        // we want to check the inverted value
        // of the starting variable
        uncheckedIndex = levelStartIndex
    }
}