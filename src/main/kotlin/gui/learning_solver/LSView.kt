package gui.learning_solver

import gui.components.AssignmentsPane
import gui.components.ClausesPane
import gui.components.ImportBanner
import gui.styles.TabStyles
import javafx.event.EventTarget
import javafx.scene.layout.BorderPane
import javafx.scene.layout.Priority
import tornadofx.*

/**
 * Window that visualizes LearningSolver
 * algorithm.
 */
class LSView : View("Solver") {
    /**
     * Corresponding controller that handles main
     * actions
     */
    private val controller: LSController by inject()

    /**
     * Pane that shows the clauses
     */
    private var clausesPane = ClausesPane()

    /**
     * Illustrates the assignments and their levels
     */
    private val assignmentsPane = AssignmentsPane {
        controller.task?.let {
            illustrate(it.names, it.view)
        }
    }

    /**
     * The grid with the three main sections
     */
    private val solverPane = gridpane {
        row {
            tab(messages["tab_cnf"]) {
                addClass(TabStyles.dark)

                center = clausesPane

                bottom = stackpane {
                    button(messages["button_change_file"]) {
                        action {
                            importBanner.show()
                        }
                    }

                    style {
                        padding = box(1.em)
                    }
                }
            }

            tab(messages["tab_implication_graph"]) {
                addClass(TabStyles.light)
                center = assignmentsPane
            }
        }

        constraintsForRow(0).percentHeight = 100.0
        constraintsForColumn(0).percentWidth = 30.0
        constraintsForColumn(1).hgrow = Priority.ALWAYS
    }

    /**
     * Constructs a tab with a title
     */
    private fun EventTarget.tab(title: String, op: BorderPane.() -> Unit) = borderpane {
        addClass(TabStyles.tab)
        top = tabTitle(title)
        apply(op)
    }

    /**
     * Constructs a title. StackPane is used
     * for centering
     */
    private fun tabTitle(title: String) = stackpane {
        label(title).addClass(TabStyles.title)
    }

    /**
     * Prompts the user to select
     * a file with the task
     */
    val importBanner = ImportBanner(messages["text_choose_file"]) {
        controller.checkSelectedFile(it)
    }

    override val root = stackpane {
        add(solverPane)
        add(importBanner.root)
    }

    init {
        importBanner.show()
    }

    /**
     * Notifies the view that it's now
     * safe to load the formula layout
     */
    fun illustrate() {
        controller.task?.let {
            clausesPane.illustrate(it.names, it.view)
            assignmentsPane.reset()
            assignmentsPane.illustrate(it.names, it.view)
        }

        importBanner.hide()
    }
}