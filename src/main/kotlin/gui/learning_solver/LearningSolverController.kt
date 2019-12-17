package gui.learning_solver

import sat.constructor.Formula
import sat.constructor.Variable
import sat.loaders.DIMACSLoader
import tornadofx.Controller
import java.io.File

/**
 * Handles the functionality of LearningSolverView
 */
class LearningSolverController : Controller() {
    /**
     * Corresponding view
     */
    private val view: LearningSolverView by inject()

    /**
     * If the user has selected a task
     * then it's contents are linked here
     */
    class Task(
        /**
         * If the user has entered a formula
         * then it's linked here
         */
        var formula: Formula,
        /**
         * If the user has entered a formula
         * then it's name mapping is linked here
         */
        var names: Map<Variable, String>
    )

    /**
     * The current task
     */
    var task: Task? = null

    /**
     * Checks if a file can be used as a
     * valid formula or not. If it can, shows
     * the formula. Prompts to select another file
     * otherwise
     */
    fun checkSelectedFile(file: File) {
        runAsync {
            try {
                val formulas = DIMACSLoader.load(file)

                if (formulas.isNotEmpty()) {
                    task = Task(formulas.first().first, formulas.first().second)
                    return@runAsync true to ""
                }
            } catch (e: Exception) {
                return@runAsync false to "${e.message}. Please select another file"
            }

            return@runAsync false to "No formulas in file!"
        } ui {
            // succeeded?
            if (it.first) {
                view.displayFormula()
            } else {
                // show message
                view.importBanner.message = it.second
            }
        }
    }
}