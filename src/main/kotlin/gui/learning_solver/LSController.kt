package gui.learning_solver

import sat.cdcl.LearningSolver
import sat.cdcl.LoggingCachingLearningView
import sat.constructor.Formula
import sat.constructor.Solution
import sat.constructor.Variable
import sat.loaders.DIMACSLoader
import tornadofx.Controller
import tornadofx.get
import java.io.File

/**
 * Handles the functionality of LearningSolverView
 */
class LSController : Controller() {
    /**
     * Corresponding view
     */
    private val view: LSView by inject()

    /**
     * If the user has selected a task
     * then it's contents are linked here
     */
    class Task(
        /**
         * If the user has entered a formula
         * then it's linked here
         */
        val formula: Formula,
        /**
         * If the user has entered a formula
         * then it's name mapping is linked here
         */
        val names: Map<Variable, String>,
        /**
         * Inner state representation
         */
        val view: LoggingCachingLearningView,
        /**
         * Possible solution if present
         */
        val solution: Solution?
    )

    /**
     * The current task
     */
    var task: Task? = null

    private fun solve(formula: Pair<Formula, Map<Variable, String>>) {
        val expression = LoggingCachingLearningView(formula.first)
        val solution = LearningSolver.solve(expression)

        task = Task(
            formula.first,
            formula.second,
            expression,
            solution
        )
    }

    /**
     * Checks if a file can be used as a
     * valid formula or not. If it can, shows
     * the formula. Prompts to select another file
     * otherwise
     */
    fun checkSelectedFile(file: File) {
        // show message
        view.importBanner.message = messages["text_solving"]

        runAsync {
            try {
                solve(DIMACSLoader.load(file))
            } catch (e: Exception) {
                return@runAsync false to "${e.message}. ${messages["text_change_file"]}"
            }

            return@runAsync true to ""
        } ui {
            // succeeded?
            if (it.first) {
                view.illustrate()
            } else {
                // show message
                view.importBanner.message = it.second
            }
        }
    }
}