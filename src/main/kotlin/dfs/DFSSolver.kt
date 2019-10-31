package dfs

import general.Formula
import general.Solver
import java.util.*
import kotlin.collections.ArrayList

/**
 * A solver that utilizes a dfs and
 * performs contradiction detection
 */
@Suppress("FunctionName")
class DFSSolver : Solver {
    /**
     * Maps a variable index to
     * it's user-defined name
     */
    private val variableNames = ArrayList<String>()

    /**
     * Contains all variables
     */
    private val variables = ArrayList<ShiftedVariable>()

    /**
     * A sugar shortcut for variable definition.
     * Introduces automatic indexing
     */
    override fun Variable(name: String): ShiftedVariable {
        // general.Variable indices are assigned starting with
        // 0 and going up for all integer values.
        val variable = ShiftedVariable(variables.size)
        variableNames.add(name)
        variables.add(variable)
        return variable
    }

    /**
     * Returns true if a contradiction is detected
     */
    private fun contradicts(
        formula: Formula<ShiftedLiteral>,
        values: BooleanArray,
        checkedCount: Int
    ): Boolean {
        for (clause in formula.clauses) {
            var canHaveNonFalseLiteral = false

            for (literal in clause.literals) {
                if (
                    literal.index >= checkedCount ||
                    literal.isPositive == values[literal.index]
                ) {
                    canHaveNonFalseLiteral = true
                    break
                }
            }

            if (!canHaveNonFalseLiteral) {
                return true
            }
        }

        return false
    }

    /**
     * Returns the list of literals describing
     * the desired values for each variable or
     * null if no solution found
     */
    fun solve(formula: Formula<ShiftedLiteral>): Set<ShiftedLiteral>? {
        // assigned values for all variables
        val values = BooleanArray(variables.size)
        // what index we have reached so far
        var checkedCount = 0

        // for dfs
        val nodes = LinkedList<ShiftedLiteral>()
        nodes.add(ShiftedLiteral(0))
        nodes.add(ShiftedLiteral(1))

        while (nodes.isNotEmpty()) {
            val next = nodes.removeLast()

            // try assign one more value
            values[next.index] = next.isPositive
            checkedCount++

            when {
                // possible false assignment, go back
                contradicts(formula, values, checkedCount) ->  {
                    checkedCount--
                }

                // all variables assigned
                checkedCount >= variables.size -> {
                    return values
                        .mapIndexed { index, isPositive ->
                            if (isPositive)
                                ShiftedLiteral(index shl 1)
                            else
                                ShiftedLiteral((index shl 1) + 1)
                        }
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

    /**
     * Returns a better string representation
     * for a formula
     */
    fun represent(formula: Formula<ShiftedLiteral>) = formula.represent {
        if (it.isPositive)
            variableNames[it.index]
        else
            "~" + variableNames[it.index]
    }

    /**
     * Returns a better string representation
     * for a solution
     */
    fun represent(solution: Set<ShiftedLiteral>) = solution.joinToString(" * ") {
        if (it.isPositive)
            variableNames[it.index]
        else
            "~" + variableNames[it.index]
    }
}