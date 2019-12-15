package gui.debug

import javafx.beans.Observable
import javafx.scene.control.Label
import tornadofx.View
import tornadofx.label
import tornadofx.singleAssign
import tornadofx.stackpane

/**
 * Prints the dimensions of the
 * primary stage
 */
@Suppress("unused")
class SizeTestingView : View("Size Testing") {
    /**
     * Shows the size message
     */
    private var sizeLabel: Label by singleAssign()

    override val root = stackpane {
        sizeLabel = label(makeSizeMessage())
    }

    init {
        val update = { _ : Observable ->
            updateSizeMessage()
        }

        primaryStage.widthProperty().addListener(update)
        primaryStage.heightProperty().addListener(update)
    }

    /**
     * Creates a size message based on the current dimensions
     */
    private fun makeSizeMessage() = "Size = (${primaryStage.width}, ${primaryStage.height})"

    /**
     * Updates contents of sizeLabel
     */
    private fun updateSizeMessage() {
        sizeLabel.text = makeSizeMessage()
    }
}