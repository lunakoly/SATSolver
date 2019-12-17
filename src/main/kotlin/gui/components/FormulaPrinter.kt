package gui.components

import gui.learning_solver.LearningSolverController
import javafx.scene.Parent
import sat.constructor.Formula
import sat.constructor.Variable
import tornadofx.UIComponent
import tornadofx.vbox

/**
 * Shows the clauses
 */
class FormulaPrinter() : UIComponent() {
    /**
     * The current task
     */
    private var task: LearningSolverController.Task? = null

    override val root = vbox {

    }

    /**
     * Loads the formula into itself
     * and displays it
     */
    fun display(task: LearningSolverController.Task) {
        this.task = task
    }
}