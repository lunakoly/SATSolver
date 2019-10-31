package watchlist

import dfs.DFSSolver
import shifted.ShiftedLiteral
import general.Clause
import general.Formula
import java.util.*

/**
 * A solver that utilizes a dfs and
 * performs contradiction detection
 * via watching for each individual
 * literals occurrence in clauses
 */
@Suppress("FunctionName")
class WatchlistSolver : DFSSolver() {
    /**
     * Returns true if a contradiction is detected.
     * Contradiction detection is done via counting
     * contradicting assignments per each clause
     *
     *   Time Complexity: O(m),  m - number of clauses for the inverse literal
     * Memory Complexity: Θ(1)
     */
    private fun contradicts(
        watchlist: Array<MutableSet<Clause<ShiftedLiteral>>>,
        falseAssignments: HashMap<Clause<ShiftedLiteral>, Int>,
        next: ShiftedLiteral
    ): Boolean {
        // Θ(number of entries for the next literal)
        for (clause in watchlist[next.invertedValue]) {
            // we know to have defined falseAssignments for
            // each single clause
            val falseAssignmentsCount = falseAssignments[clause]!! + 1
            falseAssignments[clause] = falseAssignmentsCount

            if (falseAssignmentsCount == clause.literals.size) {
                return true
            }
        }

        return false
    }

    /**
     * Returns the set of literals describing
     * the desired values for each variable or
     * null if no solution found
     *
     *   Time Complexity: O(2^k * m), n - number of clauses
     * Memory Complexity: O(k),       m - average count of entries of each literal
     *                                k - number of variables
     */
    override fun solve(formula: Formula<ShiftedLiteral>): Set<ShiftedLiteral>? {
        // maps literals to the clauses they appear in
        // O(number of variables * average count of entries of each literal)
        val watchlist = Array(variables.size * 2) {
            emptySet<Clause<ShiftedLiteral>>().toMutableSet()
        }

        // number of variables with
        // guaranteed false value per clause
        // that contains a certain literal
        // Θ(number of clauses)
        val falseAssignments = HashMap<Clause<ShiftedLiteral>, Int>()

        // fill in the watchlist
        // Θ(number of clauses)
        for (clause in formula.clauses) {
            // clear
            falseAssignments[clause] = 0

            // Θ(number of literals per clause)
            for (literal in clause.literals) {
                watchlist[literal.value].add(clause)
            }
        }

        // assigned values for all variables
        // Θ(number of variables)
        val values = BooleanArray(variables.size)
        // what index we have reached so far
        var checkedCount = 0

        // for dfs
        // at any time this checklist will
        // contain no more than (number of variables + 1) nodes
        // O(number of variables)
        val nodes = LinkedList<ShiftedLiteral>()
        nodes.add(ShiftedLiteral(0))
        nodes.add(ShiftedLiteral(1))

        // O(2^(number of variables))
        while (nodes.isNotEmpty()) {
            val next = nodes.removeLast()

            // try assign one more value
            values[next.index] = next.isPositive
            checkedCount++

            when {
                // possible false assignment, go back
                // O(m) time
                contradicts(watchlist, falseAssignments, next) ->  {
                    checkedCount--
                }

                // all variables assigned
                checkedCount >= variables.size -> {
                    return values
                        // Θ(number of variables)
                        .mapIndexed { index, isPositive ->
                            if (isPositive)
                                ShiftedLiteral(index shl 1)
                            else
                                ShiftedLiteral((index shl 1) + 1)
                        }
                        // Θ(number of variables)
                        .toSet()
                }

                else -> {
                    // add 2 possible states of the next variable
                    nodes.add(ShiftedLiteral(checkedCount shl 1))
                    nodes.add(ShiftedLiteral((checkedCount shl 1) + 1))
                }
            }
        }

        // checkedCount < variables.size
        // i. e. we couldn't assign all variables
        return null
    }
}