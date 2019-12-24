package gui.components

import gui.styles.BannerStyles
import javafx.scene.control.Label
import javafx.scene.input.DragEvent
import javafx.scene.input.MouseButton
import javafx.scene.input.MouseEvent
import javafx.scene.input.TransferMode
import javafx.stage.FileChooser
import tornadofx.*
import java.io.File

/**
 * Prompts user to select a file
 * via double click or drag and drop
 */
class ImportBanner(
    /**
     * Message to show to the user
     */
    private val defaultMessage: String,
    /**
     * Function that will be called
     * once a user selects at least one file
     */
    callback: (File) -> Unit
) : UIComponent() {
    /**
     * Link to the label that prints
     * the message
     */
    private var messageLabel: Label by singleAssign()

    override val root = stackpane {
        addClass(BannerStyles.importBanner)

        messageLabel = label(defaultMessage)

        addEventHandler(MouseEvent.MOUSE_CLICKED) {
            if (it.button == MouseButton.PRIMARY && it.clickCount == 2) {
                val file = FileChooser().showOpenDialog(primaryStage)

                if (file != null) {
                    callback(file)
                }
            }
        }

        addEventHandler(DragEvent.DRAG_OVER) {
            if (it.gestureSource == null && it.dragboard.hasFiles()) {
                it.acceptTransferModes(TransferMode.COPY)
            }
        }

        addEventHandler(DragEvent.DRAG_DROPPED) {
            if (it.dragboard.hasFiles()) {
                it.isDropCompleted = true
                callback(it.dragboard.files.first())
            } else {
                it.isDropCompleted = false
            }
        }
    }

    /**
     * A handy alias for accessing
     * the message
     */
    var message: String
        get() = messageLabel.text
        set(value) {
            messageLabel.text = value
        }

    init {
        root.isVisible = false
    }

    /**
     * Makes the banner visible. The message is
     * being reset to the default one passed to
     * the constructor
     */
    fun show() {
        root.isVisible = true
        message = defaultMessage
    }

    /**
     * Makes the banner invisible
     */
    fun hide() {
        root.isVisible = false
    }
}