package general

/**
 * Represents a disjunction of literals.
 * `T` is the type of literals
 */
class Clause<T : Literal>(vararg literals: T) {
    /**
     * Stores the literals
     */
    val literals = mutableSetOf<T>()

    init {
        this.literals.addAll(literals)
    }

    /**
     * Adds one more literal to the disjunction
     */
    operator fun plus(other: T): Clause<T> {
        literals.add(other)
        return this
    }

    /**
     * Wraps the two clauses into a
     * conjunction (a formula)
     */
    operator fun times(other: Clause<T>) = Formula(this, other)

    /**
     * Treats the `other` literal as a clause
     * and wraps the both ones into a conjunction
     */
    operator fun times(other: T) = this * Clause(other)

    /**
     * Something like `(0 + ~1 + ~2)`
     */
    override fun toString() = "(" + literals.joinToString(" + ") + ")"

    /**
     * Something like `(A + ~B + ~C)`
     */
    fun represent(decode: (T) -> String)
            = "(" + literals.joinToString(" + ") { decode(it) } + ")"
}