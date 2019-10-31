package general

/**
 * Represents a conjunction of clauses.
 * `T` is the type of literals
 */
class Formula<T : Literal>(vararg clauses: Clause<T>) {
    /**
     * Stores the clauses
     */
    val clauses = mutableSetOf<Clause<T>>()

    init {
        this.clauses.addAll(clauses)
    }

    /**
     * Adds one more clause to the conjunction
     */
    operator fun times(other: Clause<T>): Formula<T> {
        clauses.add(other)
        return this
    }

    /**
     * Adds one more single-literal
     * clause to the conjunction
     */
    operator fun times(other: T): Formula<T> {
        clauses.add(Clause(other))
        return this
    }

    /**
     * Something like `(...) * (...)`
     */
    override fun toString() = clauses.joinToString(" * ")

    /**
     * Something like `(...) * (...)`
     */
    fun represent(decode: (T) -> String)
            = clauses.joinToString(" * ") { it.represent(decode) }
}