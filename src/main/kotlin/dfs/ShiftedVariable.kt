package dfs

import general.Variable

/**
 * A variable that defines it's
 * corresponding literal states as:
 *      positive -> `index * 2`
 *      negative -> `index * 2 + 1`
 */
data class ShiftedVariable(val index: Int) : Variable {
    /**
     * Returns the literal representing
     * the variable positive state
     */
    override operator fun unaryPlus() = ShiftedLiteral(index shl 1)

    /**
     * Returns the literal representing
     * the variable negative state
     */
    override operator fun unaryMinus() = ShiftedLiteral((index shl 1) + 1)

    /**
     * Otherwise comparing collections of
     * variables becomes impossible
     */
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ShiftedVariable

        return index == other.index
    }

    /**
     * Auto-generated
     */
    override fun hashCode(): Int {
        return index
    }
}