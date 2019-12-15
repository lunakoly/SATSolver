package gui

import gui.debug.FontTestingView
import gui.debug.LearningSolverView
import gui.debug.SizeTestingView
import gui.styles.CommonStyles
import javafx.stage.Stage
import tornadofx.App

/**
 * SAT Solver visualization
 */
class Visualizer : App(LearningSolverView::class, CommonStyles::class) {
    override fun start(stage: Stage) {
        super.start(stage)

        stage.minWidth = 1200.0
        stage.minHeight = 700.0
    }
}