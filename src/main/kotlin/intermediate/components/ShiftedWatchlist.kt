package intermediate.components

import general.AbstractComponent
import intermediate.ShiftedView

/**
 * Maps each literal to the set of clauses it's met in
 */
class ShiftedWatchlist(
    /**
     * Number of variables
     */
    cardinality: Int,
    /**
     * Formula clauses
     */
    clauses: Iterable<ShiftedView.Clause>
) : AbstractComponent {
    /**
     * Associates a literal and a set of clauses
     * it is met in
     *
     * Memory Complexity: O(nm), n - cardinality
     *                           m - average number of clauses each literal
     *                               takes part in
     */
    private val watchlist = Array<MutableSet<ShiftedView.Clause>>(cardinality * 2) { mutableSetOf() }

    /**
     * Returns a set of clauses associated
     * with the given literal
     *
     *   Time Complexity: Θ(1)
     * Memory Complexity: Θ(1)
     */
    operator fun get(literal: ShiftedView.Literal) : MutableSet<ShiftedView.Clause> {
        return watchlist[literal.value]
    }

    init {
        // O(nm), n - number of clauses
        //        m - average number of literals per clause
        for (clause in clauses) {
            // Θ(number of literals per clause)
            for (literal in clause.literals) {
                watchlist[literal.value].add(clause)
            }
        }
    }
}