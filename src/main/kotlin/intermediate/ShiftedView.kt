package intermediate

import constructor.Formula
import general.AbstractClause
import general.AbstractLiteral
import general.AbstractVariable
import general.AbstractView

/**
 * Assigns each variable a unique index
 * starting with 0 and each positive literal
 * is calculated as (variable.index * 2) and
 * negative as (variable.index * 2 + 1)
 */
open class ShiftedView(
    /**
     * An input formula constructed by
     * a user
     */
    formula: Formula
) : AbstractView {
    /**
     * A variable that is backed up by an index
     */
    class Variable(
        /**
         * Some integer that represents the
         * variable.
         */
        val index: Int
    ) : AbstractVariable {
        override fun unaryPlus() = Literal(index * 2)
        override fun unaryMinus() = Literal(index * 2 + 1)
        override fun toLiteral(isPositive: Boolean) = Literal(index * 2 + if (isPositive) 0 else 1)

        override fun equals(other: Any?): Boolean {
            if (
                other is Variable &&
                index == other.index
            ) {
                return true
            }

            return false
        }

        override fun hashCode(): Int {
            return index
        }

        override fun toString(): String {
            return index.toString()
        }
    }

    /**
     * Maps each outer variable instance
     * to it's inner analogue
     */
    private val innerVariables = mutableMapOf<constructor.Variable, Variable>()

    /**
     * Maps each inner variable instance
     * to it's outer analogue
     */
    private val outerVariables = mutableMapOf<Variable, constructor.Variable>()

    /**
     * A handy alias
     */
    private fun constructor.Variable.toInner(): Variable {
        return innerVariables[this]!!
    }

    /**
     * A handy alias
     */
    protected fun Variable.toOuter(): constructor.Variable {
        return outerVariables[this]!!
    }

    /**
     * A literal that is backed up by a variable
     * with an index
     */
    class Literal(
        /**
         * Some integer that represents the literal
         * and allows to easily calculate the
         * variable it's backed by
         */
        val value: Int
    ) : AbstractLiteral {
        /**
         * The value of the inversion
         */
        val invertedValue: Int
            get() = value xor 1

        override val inversion: Literal
            get() = Literal(invertedValue)

        /**
         * The index of the underlying variable
         */
        val index: Int
            get() = value / 2

        override val variable: Variable
            get() = Variable(index)

        override val isPositive: Boolean
            get() = value and 1 == 0

        override fun equals(other: Any?): Boolean {
            if (
                other is Literal &&
                value == other.value
            ) {
                return true
            }

            return false
        }

        override fun hashCode(): Int {
            return value
        }

        override fun toString(): String {
            return if (isPositive)
                "+$index ($value)"
            else
                "-$index ($value)"
        }
    }

    /**
     * Maps an inner literal to it's
     * outer analogue
     */
    protected fun Literal.toOuter(): constructor.Literal {
        val outerVariable = outerVariables[this.variable]!!
        return outerVariable.toLiteral(this.isPositive)
    }

    /**
     * A Clause of `shifted` literals
     */
    class Clause(vararg literals: Literal) : AbstractClause {
        /**
         * Stores the literals
         */
        override val literals = mutableSetOf<Literal>()

        init {
            this.literals.addAll(literals)
        }

        override fun toString(): String {
            return represent { it.toString() }
        }
    }

    /**
     * For the sake of simplicity this
     * is technically an alias for formula's
     * clauses but with handy optimizations
     */
    val clauses = mutableSetOf<Clause>()

    /**
     * A handy alias for the number of
     * variables
     */
    protected val cardinality
        get() = outerVariables.size

    init {
        for (clause in formula.clauses) {
            val innerClause = Clause()

            for (literal in clause.literals) {
                val outerVariable = literal.variable

                if (innerVariables[outerVariable] == null) {
                    innerVariables[outerVariable] = Variable(cardinality)
                }

                val innerVariable = outerVariable.toInner()
                outerVariables[innerVariable] = outerVariable

                innerClause.literals.add(
                    innerVariable.toLiteral(literal.isPositive)
                )
            }

            clauses.add(innerClause)
        }

//        println("   Inner: " + clauses.joinToString(" * ") {
//            it.represent { variable ->
//                variable.toString()
//            }
//        })
    }
}