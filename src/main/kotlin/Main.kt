import constructor.Variable
import general.AbstractVariable

fun main() {
    val a = Variable()
    val b = Variable()
    val c = Variable()

    val expression = (+a + -b + -c) * (+b + -c) * +c

    val names = mapOf<AbstractVariable, String>(
        a to "A",
        b to "B",
        c to "C"
    )

    println("Representation: " + expression.represent(names))

    println("      One more: " + expression.represent {
        names[it] ?: "<undefined>"
    })
}