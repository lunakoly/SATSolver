package primitive

import constructor.Formula
import intermediate.ShiftedView
import java.util.*

/**
 * Assigns each variable a unique index
 * starting with 0 and each positive literal
 * is calculated as (variable.index * 2) and
 * negative as (variable.index * 2 + 1).
 *
 * Utilizes a stack for dfs search over
 * the possible solutions and a watchlist
 * lookup to boost contradiction search
 */
class PrimitiveView(
    /**
     * An input formula constructed by
     * a user
     */
    formula: Formula
) : ShiftedView(formula) {
    /**
     * The stack of nodes worth checking for
     * primitive dfs solvers
     */
    private val checkStack = LinkedList<Literal>()

    /**
     * Returns true if there're some
     * more literals that can be checked
     */
    fun hasNextLiteral() = checkStack.isNotEmpty()

    /**
     * Returns the next literal the solver needs to check
     */
    fun getNextLiteral(): Literal = checkStack.removeLast()

    /**
     * Seeks for more literals to check
     */
    fun pickNextLiterals() {
        checkStack.add(Literal(checkedCount * 2))
        checkStack.add(Literal(checkedCount * 2 + 1))
    }

    /**
     * Assigned values of variables
     */
    private val values: BooleanArray

    /**
     * Sets the value of some variable
     */
    fun setValueOf(variable: Variable, isPositive: Boolean) {
        values[variable.index] = isPositive
    }

    /**
     * Returns if the value of some set variable
     * is positive
     */
    fun getValueOf(variable: Variable): Boolean {
        return values[variable.index]
    }

    /**
     * Returns the set of literals that
     * satisfy the formula if called at the
     * right time
     */
    fun exportSolution(): Set<constructor.Literal> {
        return values
            .mapIndexed { index, isPositive ->
                Variable(index).toOuter().toLiteral(isPositive)
            }
            .toSet()
    }

    /**
     * what index we have reached so far
     */
    private var checkedCount = 0

    /**
     * Marks the literal as an already checked one.
     * This function may have significant side-effects
     * and mark other literals as already checked ones
     */
    fun check(literal: Literal) {
        checkedCount = literal.index + 1
    }

    /**
     * Marks the literal as an unchecked one.
     * This function may have significant side-effects
     * and mark other literals as already unchecked ones
     */
    fun backtrack(literal: Literal) {
        checkedCount = literal.index
    }

    /**
     * Returns true if a literal value has already
     * been examined and assigned
     */
    fun haveAlreadyChecked(literal: Literal): Boolean {
        return literal.index >= checkedCount
    }

    /**
     * Returns true if all literals are checked
     */
    fun haveCheckedEverything() = checkedCount >= cardinality

    /**
     * Associates a literal and a set of clauses
     * it is met in
     */
    private val watchlist: Array<MutableSet<Clause>>

    /**
     * Returns a set of clauses associated
     * with the inversion of the given literal
     */
    fun getRelativeClauses(literal: Literal): Set<Clause> {
        return watchlist[literal.invertedValue]
    }

    init {
        // first variable possible states
        checkStack.add(Literal(0))
        checkStack.add(Literal(1))

        values = BooleanArray(cardinality)

        watchlist = Array(cardinality * 2) {
            mutableSetOf<Clause>()
        }

        // fill the watchlist
        for (clause in clauses) {
            // Θ(number of literals per clause)
            for (literal in clause.literals) {
                watchlist[literal.value].add(clause)
            }
        }
    }
}