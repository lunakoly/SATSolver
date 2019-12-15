package sat.intermediate.components

import sat.general.AbstractComponent
import sat.intermediate.ShiftedView

/**
 * Maps each literal to the set of clauses it's met in
 */
open class ShiftedWatchlist(
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
    protected val watchlist = Array<MutableSet<ShiftedView.Clause>>(cardinality * 2) { mutableSetOf() }

    /**
     * Returns a set of clauses associated
     * with the given literal
     *
     *   Time Complexity: Θ(1)
     * Memory Complexity: Θ(1)
     */
    open operator fun get(literal: ShiftedView.Literal) : MutableSet<ShiftedView.Clause> {
        return watchlist[literal.value]
    }

    /**
     * Registers a new clause
     *
     *   Time Complexity: Θ(n), n - number of literals per clause
     * Memory Complexity: Θ(1)
     */
    fun learn(clause: ShiftedView.Clause) {
        // Θ(number of literals per clause)
        for (literal in clause.literals) {
            watchlist[literal.value].add(clause)
        }
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