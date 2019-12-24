package gui.styles

import javafx.scene.Cursor
import javafx.scene.effect.DropShadow
import javafx.scene.paint.Color
import tornadofx.*

/**
 * Defines common css rules
 */
class CommonStyles : Stylesheet() {
    companion object {
        // "Greek Diner Inline TT"
        // "Hansen"
        // "Harrington"
        // "Jokerman"
        const val FONT = "Bauhaus 93"

        val BUTTON_TEXT = c("#444444")

        val ACCENT = c("#e8e797")
        val ACCENT_HOVER = c("#e8e7a5")
        val ACCENT_ACTIVE = c("#edecaf")

        val SHADOW = c("#555555")
    }

    init {
        button {
            backgroundColor += ACCENT
            textFill = BUTTON_TEXT

            backgroundRadius += box(4.px)
            borderRadius += box(4.px)

            padding = box(
                top = 0.5.em,
                left = 1.5.em,
                right = 1.5.em,
                bottom = 0.5.em
            )

            fontSize = 2.em
            fontFamily = FONT

            cursor = Cursor.HAND

            val shadow = DropShadow()
            shadow.radius = 0.0
            shadow.offsetX = 0.0
            shadow.offsetY = 2.0
            shadow.color = SHADOW
            effect = shadow

            and(hover) {
                backgroundColor += ACCENT_HOVER

//                scaleX = 1.05
//                scaleY = 1.05
            }

            and(pressed) {
                backgroundColor += ACCENT_ACTIVE
            }
        }

        scrollPane {
            backgroundColor += Color.TRANSPARENT

            viewport {
                backgroundColor += Color.TRANSPARENT
            }
        }

        scrollBar {
            backgroundColor += Color.TRANSPARENT

            thumb {
                backgroundRadius += box(7.px)
            }

            padding = box(
                top = 0.0.px,
                left = 3.0.px,
                right = 3.0.px,
                bottom = 0.0.px
            )

            s(incrementArrow, decrementArrow) {
                padding = box(0.px)
            }

            and(vertical) {
                s(incrementButton, decrementButton) {
                    padding = box(
                        top = 0.0.px,
                        left = 0.0.px,
                        right = 14.0.px,
                        bottom = 0.0.px
                    )
                }
            }
        }
    }
}