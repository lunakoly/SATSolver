import general.Clause

fun main() {
    sat {
        val a = Variable("A")
        val b = Variable("B")
        val c = Variable("C")

        val expression = ((+a) + (-b) + (-c)) * ((+b) + (-c)) * Clause(+c)

        println("Representation: " + represent(expression))
        println("     // Source: $expression")

        val solution = solve(expression)

        if (solution != null) {
            println("Solution: " + represent(solution))
        } else {
            println("No solutions found")
        }
    }
}