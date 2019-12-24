package gui.styles

import javafx.geometry.Pos
import tornadofx.*

/**
 * Defines the styles for the clauses pane
 */
class ClausesListStyles : Stylesheet() {
    companion object {
        val TEXT = c("#111111")
        val ACCENT_TEXT = c("#9c9a4e")

        val clausesList by cssclass()

        val learned by cssclass()
    }

    init {
        clausesList {
            spacing = 0.2.em

            fontFamily = "Source Code Pro"
            textFill = TEXT

//            padding = box(0.5.em)

            label {
                alignment = Pos.CENTER

                padding = box(
                    top = 0.2.em,
                    left = 0.2.em,
                    right = 0.2.em,
                    bottom = 0.2.em
                )
            }

            label and learned {
                textFill = ACCENT_TEXT
            }
        }
    }
}