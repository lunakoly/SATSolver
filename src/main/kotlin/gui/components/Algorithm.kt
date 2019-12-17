package gui.components

import gui.styles.CommonStyles
import javafx.scene.control.Label
import tornadofx.*

/**
 * A portion of source code that can
 * highlight a line
 */
class Algorithm(
    /**
     * The file to load the algorithm from
     */
    filename: String,
    /**
     * The max width of each line in the
     * resulting element
     */
    maximumWidth: Dimension<Dimension.LinearUnits>? = null
) : UIComponent() {
    /**
     * List of available lines
     */
    private val lines = ArrayList<Label>()

    override val root = vbox {
        addClass(CommonStyles.code)
        // TODO: check TextFlow

        resources.text(filename)
            .replace("\r", "")
            .split("\n")
            .forEach {
                // automatically added to vbox
                lines.add(label(it))
            }

        if (lines.isNotEmpty()) {
            lines.first().addClass(CommonStyles.selected)
        }

        if (maximumWidth != null) {
            style {
                maxWidth = 25.em
            }
        }
    }
}