package intermediate.components

import intermediate.ShiftedView

/**
 * Maps each literal to the set of clauses it's met in
 * and can skip already approved clauses
 */
class LazyWatchlist(
    /**
     * Number of variables
     */
    cardinality: Int,
    /**
     * Formula clauses
     */
    clauses: Iterable<ShiftedView.Clause>
) : ShiftedWatchlist(cardinality, clauses) {
    /**
     * Maps approved clauses to the literals
     * that watch them. Approved clauses are
     * removed from the watchlist so that they
     * will not be iterated during assignments
     *
     * Memory Complexity: O(nm), n - number of clauses
     *                           m - average number of literals in each clause
     */
    private val approved = mutableMapOf<ShiftedView.Clause, MutableSet<ShiftedView.Literal>>()

    /**
     * Marks the clause as 'approved' and
     * it will not be iterated during variable
     * assignments
     *
     *   Time Complexity: O(n), n - average number of literals per clause
     * Memory Complexity: O(n)
     */
    fun approve(clause: ShiftedView.Clause) {
        // O(average number of literals per clause)
        val literals = mutableSetOf<ShiftedView.Literal>()
        approved[clause] = literals

        // O(average number of literals per clause)
        for (literal in clause.literals) {
            literals.add(literal)
            watchlist[literal.value].remove(clause)
        }
    }

    /**
     * Marks the clause as 'approved' and
     * it will not be iterated during variable
     * assignments
     *
     *   Time Complexity: O(nm), n - number of clauses
     * Memory Complexity: O(m)   m - average number of literals per clause
     */
    fun updateApproved(
        /**
         * Some rule that maps a clause to the number
         * of approved variables in it
         */
        countApproved: (ShiftedView.Clause) -> Int
    ) {
        val iterator = approved.iterator()

        // O(count of approved * average number of literals per clause)
        while (iterator.hasNext()) {
            val (clause, literals) = iterator.next()
            val approvedCount = countApproved(clause)

            if (approvedCount == 0) {
                iterator.remove()

                // O(average number of literals per clause)
                for (literal in literals) {
                    watchlist[literal.value].add(clause)
                }
            }
        }
    }

    /**
     * Returns a set of clauses associated
     * with the given literal
     *
     *   Time Complexity: Θ(1)
     * Memory Complexity: Θ(1)
     */
    override operator fun get(literal: ShiftedView.Literal) : MutableSet<ShiftedView.Clause> {
        return watchlist[literal.value]
    }
}