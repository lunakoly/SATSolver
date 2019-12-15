package sat.constructor

import sat.general.AbstractVariable

/**
 * Represents a variable that a one may utilize
 * to construct a formula. A variable represents
 * a superposition of the possible states
 * (true & false). To define the exact value (turn
 * a variable into a literal) the one needs to
 * apply unary plus or minus to it
 */
class Variable : AbstractVariable {
    override operator fun unaryPlus() = Literal(this, true)
    override operator fun unaryMinus() = Literal(this, false)
    override fun toLiteral(isPositive: Boolean) = Literal(this, isPositive)
}