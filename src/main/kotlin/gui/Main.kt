package gui

import sat.cdcl.CachingLearningView
import sat.cdcl.LearningSolver
import sat.constructor.Variable
import tornadofx.launch

fun main(args: Array<String>) {
    val a = Variable()
    val b = Variable()
    val c = Variable()

    val expression = (+a + -b + -c) * (+b + -c) * +c

    val names = mapOf(
        a to "A",
        b to "B",
        c to "C"
    )

    println("Representation: " + expression.represent(names))

    println("      One more: " + expression.represent {
        names[it] ?: "<undefined>"
    })

    val view = CachingLearningView(expression)
    val solution = LearningSolver.solve(view)

    if (solution != null) {
        println("      Solution: " + solution.represent(names))
        println("      One more: " + solution.represent {
            names[it] ?: "<undefined>"
        })
    } else {
        print("      Solution: Not found")
    }

    launch<Visualizer>(args)
}