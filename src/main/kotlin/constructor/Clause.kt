package constructor

import general.AbstractClause

/**
 * Represents a disjunction of literals.
 * and may be used to construct a formula
 */
class Clause(
    /**
     * Literals initially added to the clause
     */
    vararg literals: Literal
) : AbstractClause {
    override val literals = mutableSetOf<Literal>()

    init {
        this.literals.addAll(literals)
    }

    operator fun plus(other: Literal): Clause {
        literals.add(other)
        return this
    }

    operator fun times(other: Clause) = Formula(this, other)

    operator fun times(other: Literal) = this * Clause(other)
}