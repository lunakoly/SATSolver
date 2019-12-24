package gui.debug

import javafx.event.EventTarget
import javafx.geometry.Pos
import javafx.scene.text.Font
import tornadofx.*

/**
 * Displays all available font families
 */
@Suppress("unused")
class FontTestingView : View("Font Testing") {
    /**
     * A vertical series of labels with
     * different fonts. If parameter "text"
     * is not specified the font name is used
     * instead
     */
    private fun EventTarget.fontSeries(text: String? = null) = vbox {
        for (fontName in Font.getFamilies()) {
            var labelText = fontName

            if (text != null) {
               labelText = "$text --- $fontName"
            }

            label(labelText) {
                style {
                    fontFamily = fontName
                    fontSize = 2.em

                    alignment = Pos.CENTER
                    maxWidth = infinity
                }
            }
        }
    }

    override val root = scrollpane(true) {
        fontSeries("Граф Импликаций")
    }
}