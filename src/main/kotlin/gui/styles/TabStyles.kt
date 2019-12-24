package gui.styles

import tornadofx.*

/**
 * Defines the styles of the main tabs
 */
class TabStyles : Stylesheet() {
    companion object {
        val BACKGROUND = c("#ffffff")
        val BACKGROUND_DARK = c("#f8f8f8")

        val BORDER = c("#f0f0f0")
        val BORDER_DARK = c("#e0e0e0")

        val FOREGROUND = c("#444444")
        val FOREGROUND_DARK = c("#333333")

        val tab by cssclass()
        val title by cssclass()

        val light by cssclass()
        val dark by cssclass()
    }

    init {
        tab {
            title {
                fontSize = 2.em
                fontFamily = CommonStyles.FONT

                padding = box(
                    top = 0.5.em,
                    left = 0.em,
                    right = 0.em,
                    bottom = 0.5.em
                )

//                alignment = Pos.CENTER
//                maxWidth = infinity

                borderWidth += box(
                    top = 0.px,
                    left = 0.px,
                    right = 0.px,
                    bottom = 4.px
                )
            }
        }

        tab and light {
            backgroundColor += BACKGROUND

            title {
                borderColor += box(BORDER)
                textFill = FOREGROUND
            }
        }

        tab and dark {
            backgroundColor += BACKGROUND_DARK

            title {
                borderColor += box(BORDER_DARK)
                textFill = FOREGROUND_DARK
            }
        }
    }
}