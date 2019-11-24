package constructor

import general.AbstractFormula

/**
 * Represents a conjunction of clauses.
 * that can be used later as an input for a solver
 */
class Formula(
    /**
     * Clauses initially added to the
     * conjunction
     */
    vararg clauses: Clause
) : AbstractFormula {
    override val clauses = mutableSetOf<Clause>()

    init {
        this.clauses.addAll(clauses)
    }

    operator fun times(other: Clause): Formula {
        clauses.add(other)
        return this
    }

    operator fun times(other: Literal): Formula {
        clauses.add(Clause(other))
        return this
    }
}