package gui.learning_solver

import sat.cdcl.CachingLearningView
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
        val formula: Formula,
        /**
         * If the user has entered a formula
         * then it's name mapping is linked here
         */
        val names: Map<Variable, String>,
        /**
         * Inner state representation
         */
        val view: CachingLearningView
    )

    /**
     * The current task
     */
    var task: Task? = null

    /**
     * List of callbacks to call whenever
     * the formula gets updated
     */
    val taskUpdateListeners = ArrayList<(Task) -> Unit>()

    private fun update() {
        val task = this.task

        if (task != null) {
            taskUpdateListeners.forEach {
                it(task)
            }
        }
    }

    /**
     * Checks if a file can be used as a
     * valid formula or not. If it can, shows
     * the formula. Prompts to select another file
     * otherwise
     */
    fun checkSelectedFile(file: File) {
        runAsync {
            try {
                val formula = DIMACSLoader.load(file)

                task = Task(
                    formula.first,
                    formula.second,
                    CachingLearningView(formula.first)
                )

                return@runAsync true to ""
            } catch (e: Exception) {
                return@runAsync false to "${e.message}. Please select another file"
            }
        } ui {
            // succeeded?
            if (it.first) {
//                view.formulaPrinter.reset()
                update()
                view.displayFormula()
            } else {
                // show message
                view.importBanner.message = it.second
            }
        }
    }
}