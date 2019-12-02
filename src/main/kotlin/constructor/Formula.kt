package constructor

import general.AbstractFormula
import general.AbstractLiteral
import general.AbstractVariable

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

    override fun satisfies(solution: Set<AbstractLiteral>): Boolean {
        val assignments = mutableMapOf<AbstractVariable, Boolean>()

        for (it in solution) {
            assignments[it.variable] = it.isPositive
        }

        for (clause in clauses) {
            var satisfied = false

            for (literal in clause.literals) {
                if (assignments[literal.variable] == literal.isPositive) {
                    satisfied = true
                    break
                }
            }

            if (!satisfied) {
                return false
            }
        }

        return true
    }
}