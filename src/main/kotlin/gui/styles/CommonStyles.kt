package gui.styles

import javafx.geometry.Pos
import javafx.scene.Cursor
import javafx.scene.effect.DropShadow
import javafx.scene.paint.Color
import tornadofx.*

class CommonStyles : Stylesheet() {
    companion object {
        val BACKGROUND = c("#ffffff")
        val BACKGROUND_DARK = c("#f8f8f8")

        val BORDER = c("#f0f0f0")
        val BORDER_DARK = c("#e0e0e0")

        val FOREGROUND = c("#444444")
        val FOREGROUND_DARK = c("#333333")
        val TEXT = c("#111111")

        val ACCENT = c("#e8e797")
        val ACCENT_HOVER = c("#e8e7a5")
        val ACCENT_ACTIVE = c("#edecaf")

        val SHADOW = c("#555555")

        val tab by cssclass()
        val title by cssclass()

        val code by cssclass()

        val dark by cssclass()
        val light by cssclass()

        val selected by cssclass()
    }

    init {
        tab {
            title {
                fontSize = 2.em
                fontFamily = "Bauhaus 93"
                // "Greek Diner Inline TT"
                // "Hansen"
                // "Harrington"
                // "Jokerman"

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

        button {
            backgroundColor += ACCENT
            textFill = FOREGROUND

            backgroundRadius += box(4.px)
            borderRadius += box(4.px)

            padding = box(
                top = 0.5.em,
                left = 1.5.em,
                right = 1.5.em,
                bottom = 0.5.em
            )

            fontSize = 2.em
            fontFamily = "Bauhaus 93"

            cursor = Cursor.HAND

            val shadow = DropShadow()
            shadow.radius = 0.0
            shadow.offsetX = 0.0
            shadow.offsetY = 2.0
            shadow.color = SHADOW
            effect = shadow

            and(hover) {
                backgroundColor += ACCENT_HOVER
            }

            and(pressed) {
                backgroundColor += ACCENT_ACTIVE
            }
        }

        code {
            fontFamily = "Source Code Pro"
            textFill = TEXT

            padding = box(0.5.em)

            alignment = Pos.CENTER_LEFT

            label and selected {
                backgroundColor += ACCENT
                backgroundRadius += box(2.px)
            }
        }
    }
}