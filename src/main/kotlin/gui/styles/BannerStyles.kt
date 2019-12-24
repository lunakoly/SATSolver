package gui.styles

import javafx.scene.layout.BorderStrokeStyle
import tornadofx.*

/**
 * Defines the way banners look
 */
class BannerStyles : Stylesheet() {
    companion object {
        val BANNER_BORDER = c("#ffffff")

        val importBanner by cssclass()
    }

    init {
        importBanner {
            backgroundColor += CommonStyles.ACCENT

            borderWidth += box(8.px)
            borderColor += box(BANNER_BORDER)
            borderStyle += BorderStrokeStyle.DASHED

            borderInsets += box(8.px)
            borderRadius += box(8.px)

            fontSize = 2.em
            fontFamily = CommonStyles.FONT
        }
    }
}