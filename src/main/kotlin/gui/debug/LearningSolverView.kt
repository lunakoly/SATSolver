package gui.debug

import gui.styles.CommonStyles
import javafx.event.EventTarget
import javafx.scene.layout.BorderPane
import tornadofx.*

class LearningSolverView : View("Learning Solver") {
    override val root = borderpane {
        left = tab("CNF") {
            addClass(CommonStyles.dark)

            style {
                minWidth = 20.em
            }
        }

        center = tab("IMPLICATION GRAPH") {
            addClass(CommonStyles.light)
        }

        right = tab("ALGORITHM") {
            addClass(CommonStyles.dark)

            style {
                minWidth = 25.em
            }

            bottom = stackpane {
                button("Next")

                style {
                    padding = box(1.em)
                }
            }

            center = vbox {
                addClass(CommonStyles.code)
                // TODO: check TextFlow

                label("for each unit clause:")
                label("    push(first literal)")
                label("")
                label("    if controverts():")
                label("        return UNSAT")
                label("")
                label("while solution not found:")
                label("    if checked all:")
                label("        push(some new literal)")
                label("")
                label("    if controverts():").addClass(CommonStyles.selected)
                label("        if not backtrack():")
                label("            return UNSAT")
                label("    else:")
                label("        if controverts():")
                label("            learn(controverting clause)")
                label("")
                label("            if not backtrack():")
                label("                return UNSAT")
            }
        }
    }

    private fun EventTarget.tab(title: String, op: BorderPane.() -> Unit) = borderpane {
        addClass(CommonStyles.tab)
        top = tabTitle(title)
        apply(op)
    }

    private fun tabTitle(title: String) = stackpane {
        label(title).addClass(CommonStyles.title)
    }
}