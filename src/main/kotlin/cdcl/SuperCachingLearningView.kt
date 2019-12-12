package cdcl

import constructor.Formula

/**
 * LearningView with caching levels start
 * and end indices
 */
class SuperCachingLearningView(
    /**
     * An input formula constructed by
     * a user
     */
    formula: Formula
) : CachingLearningView(formula) {
    /**
     * Points to the first literal
     * of some level
     */
    private val levelsStarts = IntArray(cardinality) { -1 }

    /**
     * Points to the last literal of
     * some level
     */
    private val levelsEnds = IntArray(cardinality) { -1 }

    override fun findLevelStart(index: Int) = levelsStarts[levels[index] + 1]

    override fun findLevelEnd(index: Int) = levelsEnds[levels[index] + 1]

    override fun check(): CheckResult {
        val literal = values[uncheckedIndex]

        if (assignments[literal.index] == literal.isPositive) {
            // due to the algorithm it can only happen
            // if literal.isPositive != other.isPositive
            // so we need to backtrack.
            // DOUBLE INSERTIONS ARE PREVENTED
            // INSIDE PUSH()
            return CheckResult.DUPLICATE
        }

        uncheckedIndex += 1
        assignments[literal.index] = literal.isPositive
        levelsEnds[levels[uncheckedIndex - 1] + 1] = uncheckedIndex - 1
        return CheckResult.OK
    }

    override fun push(literal: Literal, origin: Clause?) {
        // prevent multiple exertions
        if (!pushes.contains(literal)) {
            // to not have a deduction origin == to be selected manually
            if (origin == null) {
                nextLevel += 1
                levelsStarts[nextLevel + 1] = nextIndex
            }

            values[nextIndex] = literal
            levels[nextIndex] = nextLevel
            origins[nextIndex] = origin
            pushes.add(literal)

            nextIndex += 1
        }
    }
}