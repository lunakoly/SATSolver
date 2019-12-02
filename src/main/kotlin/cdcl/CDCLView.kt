package cdcl

import constructor.Formula
import intermediate.ShiftedView
import intermediate.components.ShiftedWatchlist

/**
 * Assigns each variable a unique index
 * starting with 0 and each positive literal
 * is calculated as (variable.index * 2) and
 * negative as (variable.index * 2 + 1).
 *
 * Performs literal deduction and learns
 * contradictions (MODIFIES THE FORMULA)
 */
class CDCLView(
    /**
     * An input formula constructed by
     * a user
     */
    formula: Formula
) : ShiftedView(formula) {
    private fun isApproved(literal: Literal): Boolean {
        for (it in index - 1 downTo 0) {
            if (values[it].index == literal.index) {
                return values[it] == literal
            }
        }

        return false
    }

    private fun isAssigned(variable: Variable): Boolean {
        for (it in index - 1 downTo 0) {
            if (values[it].index == variable.index) {
                return true
            }
        }

        return false
    }

    private fun isAssigned(literal: Literal) = isAssigned(literal.variable)

    private fun evaluate(clause: Clause): Triple<Int, Literal?, Int> {
        var someUnassigned: Literal? = null
        var unassignedLeft = 0
        var approved = 0

        for (literal in clause.literals) {
            if (isApproved(literal)) {
                approved += 1
            } else if (!isAssigned(literal)) {
                someUnassigned = literal
                unassignedLeft += 1
            }
        }

        return Triple(unassignedLeft, someUnassigned, approved)
    }

    private fun analyze(clause: Clause, literal: Literal): BacktrackResult {
        val (unassignedLeft, unassigned, approved) = evaluate(clause)

        if (unassignedLeft == 0 && approved == 0) {
            if (backtrack(literal) == BacktrackResult.UNSATISFIED) {
                return BacktrackResult.UNSATISFIED
            }
        }

        // unassigned != null will always be true here
        // but let's satisfy at least the compiler
        else if (unassignedLeft == 1 && approved == 0 && unassigned != null) {
            if (push(unassigned, clause) == BacktrackResult.UNSATISFIED) {
                return BacktrackResult.UNSATISFIED
            }
        }

        return BacktrackResult.OK
    }

    /**
     * Attempts to add a clause to the formula
     */
    private fun learn(firstIndex: Int, second: Literal, secondOrigin: Clause?) {
        val firstOrigin = origins[firstIndex]

        if (firstOrigin != null && secondOrigin != null) {
            val first = values[firstIndex]

            val result = Clause()

            for (it in firstOrigin.literals) {
                if (it != first) {
                    result.literals.add(it)
                }
            }

            for (it in secondOrigin.literals) {
                if (it != second) {
                    result.literals.add(it)
                }
            }

            clauses.add(result)
        }
    }

    /**
     * Assigned values of variables.
     * +1 helps with one extra assignment backtracking
     * although we already know it's impossible
     */
    private val values: Array<Literal> = Array(cardinality + 1) { Literal(0) }

    /**
     * Index of the cell we should
     * backtrack to. Deduced literals have
     * the same level
     */
    private val levels: Array<Int> = Array(cardinality + 1) { -1 }

    /**
     * Maps an assigned value to the
     * literal it's been deduced from
     */
    private val origins: Array<Clause?> = Array(cardinality + 1) { null }

    /**
     * Points to the next empty cell
     * in values and levels
     */
    private var index = 0

    /**
     * Level assigned to all next literals
     * pushed with an origin
     */
    private var nextLevel = -1

    /**
     * Returns the set of literals that
     * satisfy the formula if called at the
     * right time
     */
    fun exportSolution(): Set<constructor.Literal> {
        return values
            .map { it.toOuter() }
            .toSet()
    }

    /**
     * Associates a literal and a set of clauses
     * it is met in
     */
    private val watchlist = ShiftedWatchlist(cardinality, clauses)

    /**
     * Returns true if there're some
     * more literals that can be checked
     */
    fun hasNextLiteral() = index < cardinality

    /**
     * Returns the next literal the solver needs to check
     */
    fun getNextLiteral(): Literal {
        for (it in 0 until cardinality) {
            val variable = Variable(it)

            if (!isAssigned(variable)) {
                return +variable
            }
        }

        throw IllegalStateException("No next elements")
    }

    enum class BacktrackResult {
        UNSATISFIED, OK
    }

    /**
     * Adds a literal to values
     */
    fun push(literal: Literal, origin: Clause? = null): BacktrackResult {
        for (it in index - 1 downTo 0) {
            val other = values[it]
            // same variable
            if (other.index == literal.index) {
                if (other.isPositive != literal.isPositive) {
                    // contradiction
                    if (backtrack(other) == BacktrackResult.UNSATISFIED) {
                        return BacktrackResult.UNSATISFIED
                    }

                    // learn clauses
                    learn(it, literal, origin)
                }
            }
        }

        values[index] = literal
        origins[index] = origin

        when {
            origin == null -> {
                nextLevel += 1
                levels[index] = nextLevel
            }

            index == 0 -> {
                levels[index] = -1
            }

            else -> {
                levels[index] = levels[index - 1]
            }
        }

        index += 1

        for (clause in watchlist[literal.inversion]) {
            if (analyze(clause, literal) == BacktrackResult.UNSATISFIED) {
                return BacktrackResult.UNSATISFIED
            }
        }

        for (clause in watchlist[literal]) {
            if (analyze(clause, literal) == BacktrackResult.UNSATISFIED) {
                return BacktrackResult.UNSATISFIED
            }
        }

        return BacktrackResult.OK
    }

    /**
     * Returns to the literal and reverts
     * everything assigned above or at the same level
     */
    private fun backtrack(literal: Literal): BacktrackResult {
        for (it in index - 1 downTo 0) {
            if (values[it] == literal) {
                if (levels[it] == -1) {
                    return BacktrackResult.UNSATISFIED
                }

                if (it == 0) {
                    levels[it] = -1
                } else {
                    levels[it] = levels[it - 1]
                }

                values[it] = values[it].inversion
                nextLevel = levels[it]
                index = it + 1

                return BacktrackResult.OK
            }
        }

        throw IllegalArgumentException("Literal is not in values")
    }
}