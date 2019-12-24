package sat.cdcl

import sat.constructor.Formula

/**
 * CachingLearningSolver that tracks
 * which clauses have been learned and is
 * capable of representing them to the user
 */
class LoggingCachingLearningView(
    /**
     * An input formula constructed by
     * a user
     */
    formula: Formula
) : CachingLearningView(formula) {
    /**
     * The list of learned clauses
     */
    private val learnedClauses = mutableSetOf<Clause>()

    override fun learn(contradictingClause: Clause?): Clause? {
        val contradictingVariable = values[uncheckedIndex - 1].variable
        val literalOrigin = origins[uncheckedIndex - 1]

        if (
            contradictingClause != null &&
            literalOrigin != null
        ) {
            // no duplicates because Clause
            // stores a set of literals
            val result = Clause()

            contradictingClause.literals
                .filter { it.variable != contradictingVariable }
                .forEach {
                    result.literals.add(it)
                }

            literalOrigin.literals
                .filter { it.variable != contradictingVariable }
                .forEach {
                    result.literals.add(it)
                }

            clauses.add(result)
            watchlist.learn(result)
            learnedClauses.add(result)
            return result
        }

        return null
    }

    /**
     * Transforms inner clauses to the outer ones
     */
    private fun Collection<Clause>.toOuter(): Set<sat.constructor.Clause> {
        return this
            .map { clause ->
                val result = sat.constructor.Clause()

                clause.literals
                    .forEach {
                        result.literals.add(it.toOuter())
                    }

                result
            }
            .toSet()
    }

    /**
     * Represents the native clauses
     * and the learned ones
     */
    fun exposeClauses(): Pair<Set<sat.constructor.Clause>, Set<sat.constructor.Clause>> {
        val nativeClauses = clauses
            .filter { it !in learnedClauses }

        return nativeClauses.toOuter() to learnedClauses.toOuter()
    }

    /**
     * Represents the values in
     * user-side entities
     */
    fun exposeValues() = values.map {
        it.toOuter()
    }
}