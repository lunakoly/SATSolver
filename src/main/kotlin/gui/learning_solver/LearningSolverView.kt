package gui.learning_solver

import gui.components.Algorithm
import gui.components.FormulaPrinter
import gui.components.ImportBanner
import gui.styles.CommonStyles
import javafx.event.EventTarget
import javafx.scene.layout.BorderPane
import javafx.scene.layout.Priority
import tornadofx.*

/**
 * Window that visualizes LearningSolver
 * algorithm.
 */
class LearningSolverView : View("Learning Solver") {
    /**
     * Corresponding controller that handles main
     * actions
     */
    private val controller: LearningSolverController by inject()

    /**
     * LearningSolver algorithm
     */
    val algorithm = Algorithm("/algorithm.txt", 25.em)

    /**
     * Displays the clauses
     */
    val formulaPrinter = FormulaPrinter()

    /**
     * The grid with the three main sections
     */
    private val solverPane = gridpane {
        row {
            tab(messages["tab_cnf"]) {
                addClass(CommonStyles.dark)

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

                center = formulaPrinter.root
            }

            tab(messages["tab_implication_graph"]) {
                addClass(CommonStyles.light)
            }

            tab(messages["tab_algorithm"]) {
                addClass(CommonStyles.dark)

                bottom = stackpane {
                    button(messages["button_next"])

                    style {
                        padding = box(1.em)
                    }
                }

                center = algorithm.root
            }
        }

        constraintsForRow(0).percentHeight = 100.0
        constraintsForColumn(0).percentWidth = 30.0
        constraintsForColumn(1).hgrow = Priority.ALWAYS
        constraintsForColumn(2).percentWidth = 32.0
    }

    /**
     * Constructs a tab with a title
     */
    private fun EventTarget.tab(title: String, op: BorderPane.() -> Unit) = borderpane {
        addClass(CommonStyles.tab)
        top = tabTitle(title)
        apply(op)
    }

    /**
     * Constructs a title. StackPane is used
     * for centering
     */
    private fun tabTitle(title: String) = stackpane {
        label(title).addClass(CommonStyles.title)
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
    fun displayFormula() {
        importBanner.hide()
    }
}