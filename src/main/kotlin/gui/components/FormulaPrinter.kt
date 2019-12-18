package gui.components

import gui.learning_solver.LearningSolverController
import gui.styles.CommonStyles
import javafx.scene.Parent
import javafx.scene.control.Label
import javafx.scene.layout.Priority
import javafx.scene.layout.Region
import javafx.scene.layout.VBox
import javafx.scene.paint.Color
import sat.constructor.Clause
import sat.constructor.Formula
import sat.constructor.Variable
import sat.intermediate.ShiftedView
import tornadofx.*

/**
 * Shows the clauses
 */
class FormulaPrinter : UIComponent() {
    private val controller: LearningSolverController by inject()

    private var clausesBox: VBox by singleAssign()

    override val root = scrollpane(true) {
        clausesBox = vbox {
            addClass(CommonStyles.clausesList)
        }
    }

    init {
        controller.taskUpdateListeners.add(::update)
    }

    /**
     * Loads the formula into itself
     * and displays it
     */
    private fun update(task: LearningSolverController.Task) {
        for (clause in task.view.exportClauses()) {
            val text = clause.represent {
                return@represent task.names[it] ?: "<unnamed>"
            }

            clausesBox.add(Label(text).apply {
                isWrapText = true
                maxWidthProperty().bind(root.widthProperty())
            })
        }
    }

    /**
     * Removes all clauses
     */
    fun reset() {
        clausesBox.children.clear()
    }
}