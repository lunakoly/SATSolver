package gui.components

import gui.styles.ClausesListStyles
import javafx.scene.control.Label
import javafx.scene.control.ScrollPane
import javafx.scene.layout.VBox
import sat.cdcl.LoggingCachingLearningView
import sat.general.AbstractVariable
import tornadofx.FX.Companion.messages
import tornadofx.add
import tornadofx.addClass
import tornadofx.clear
import tornadofx.get

/**
 * Shows the list of clauses inside
 * a LoggingCachingLearningView
 */
class ClausesPane : ScrollPane() {
    /**
     * The width of the whole pane
     */
    private val globalWidth = widthProperty()

    /**
     * Underlying VBox with
     * labels-clauses
     */
    private val vBox = VBox()

    init {
        isFitToWidth = true
        content = vBox

        vBox.addClass(ClausesListStyles.clausesList)
    }

    /**
     * Updates the list of clauses
     */
    fun illustrate(
        names: Map<out AbstractVariable, String>,
        view: LoggingCachingLearningView
    ) {
        vBox.clear()

        val (nativeClauses, learnedClauses) = view.exposeClauses()

        // add all non-learned clauses
        for (clause in nativeClauses) {
            val text = clause.represent {
                return@represent names[it] ?: messages["text_unnamed"]
            }

            vBox.add(Label(text).apply {
                isWrapText = true
                maxWidthProperty().bind(globalWidth)
            })
        }

        // add all learned clauses
        for (clause in learnedClauses) {
            val text = clause.represent {
                return@represent names[it] ?: messages["text_unnamed"]
            }

            vBox.add(Label(text).apply {
                addClass(ClausesListStyles.learned)

                isWrapText = true
                maxWidthProperty().bind(globalWidth)
            })
        }
    }
}