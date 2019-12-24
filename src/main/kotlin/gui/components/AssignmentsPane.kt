package gui.components

import javafx.scene.canvas.Canvas
import javafx.scene.layout.Pane
import javafx.scene.paint.Color
import javafx.scene.text.Font
import javafx.scene.text.Text
import javafx.scene.transform.Affine
import sat.cdcl.LoggingCachingLearningView
import sat.general.AbstractVariable

/**
 * Draws the assignments graph.
 * It's important to extend Pane
 * but not StackPane
 */
@Suppress("RedundantLambdaArrow")
class AssignmentsPane(
    /**
     * Function called whenever canvas should
     * be repainted
     */
    private val draw: AssignmentsPane.() -> Unit
) : Pane() {
    companion object {
        const val NODE_RADIUS = 30.0
    }

    /**
     * A box for 2 doubles with common
     * operations
     */
    class Vec2(var x: Double, var y: Double) {
        operator fun plus(other: Vec2) = Vec2(x + other.x, y + other.y)

        operator fun minus(other: Vec2) = Vec2(x - other.x, y - other.y)

        operator fun unaryMinus() = Vec2(-x, -y)

        override fun toString() = "($x, $y)"
    }

    /**
     * This may seem fantastic but it's reality!
     * I do multiply a 3x4 matrix by a 2x1 vector!
     * Why compute more when we can compute less?
     */
    operator fun Affine.times(vector: Vec2): Vec2 {
        return Vec2(
            mxx * vector.x + mxy * vector.y + tx,
            myx * vector.x + myy * vector.y + ty
        )
    }

    /**
     * The underlying element that
     * performs drawing
     */
    private val canvas = object : Canvas() {
        override fun isResizable() = true
    }

    init {
        children.add(canvas)

        // resizable canvas
        canvas.widthProperty().bind(widthProperty())
        canvas.heightProperty().bind(heightProperty())

        // update on resize
        canvas.widthProperty().addListener { _ -> draw() }
        canvas.heightProperty().addListener { _ -> draw() }

        // better font
        canvas.graphicsContext2D.font = Font("Source Code Pro", 26.0)

        // listen to mouse movement
        var old = Vec2(0.0, 0.0)

        canvas.setOnMousePressed {
            old = Vec2(it.x, it.y)
        }

        canvas.setOnMouseDragged {
            val new = Vec2(it.x, it.y)

            val delta = new - old
            old = new

            canvas.graphicsContext2D.translate(delta.x, delta.y)
            draw()
        }
    }

    /**
     * Restores everything to defaults
     */
    fun reset() {
        canvas.graphicsContext2D.transform = Affine()
    }

    /**
     * Clears the canvas and redraws the graph
     */
    fun illustrate(
        names: Map<out AbstractVariable, String>,
        view: LoggingCachingLearningView
    ) {
        val graphics = canvas.graphicsContext2D
        val height = height
        val width = width

        val transform = graphics.transform

        graphics.clearRect(
            -transform.tx,
            -transform.ty,
            width,
            height
        )

        val values = view.exposeValues()

        // +-1
        var displacementDirection = 1
        var heightOffset = 20.0

        // changes each time a level changes
        var levelSwitch = false
        var oldLevel = -2
        var index = 0

        while (index < view.uncheckedIndex) {
            // circle top-left corner
            val x = width / 2 - NODE_RADIUS + displacementDirection * NODE_RADIUS / 2
            val y = heightOffset

            // check levels
            if (oldLevel != view.levels[index]) {
                oldLevel = view.levels[index]
                levelSwitch = !levelSwitch
                graphics.stroke = Color.BLACK
            } else {
                graphics.stroke = Color.GRAY
            }

            // select and fill the background
            if (levelSwitch) {
                graphics.fill = Color.LIGHTBLUE
            } else {
                graphics.fill = Color.LIGHTGREEN
            }

            graphics.fillOval(x, y, NODE_RADIUS * 2, NODE_RADIUS * 2)

            // fill outline
            graphics.fill = Color.BLACK
            graphics.lineWidth = 4.0

            graphics.strokeOval(x, y, NODE_RADIUS * 2, NODE_RADIUS * 2)

            // print a literal inside
            val text = values[index].represent(names)
            val bounds = Text(text).layoutBounds

            // magic constants
            val textX = x + NODE_RADIUS - bounds.width
            val textY = y + NODE_RADIUS + bounds.height / 2.4

            graphics.fillText(text, textX, textY)

            displacementDirection *= -1
            heightOffset += NODE_RADIUS * 2 + 20
            index++
        }
    }
}