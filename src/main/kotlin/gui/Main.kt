package gui

import gui.learning_solver.LSView
import gui.styles.BannerStyles
import gui.styles.ClausesListStyles
import gui.styles.CommonStyles
import gui.styles.TabStyles
import javafx.stage.Stage
import tornadofx.App
import tornadofx.launch

/**
 * SAT Solver visualization
 */
class Main : App(
    LSView::class,
    CommonStyles::class,
    TabStyles::class,
    BannerStyles::class,
    ClausesListStyles::class
) {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            launch<Main>(args)
        }
    }

    override fun start(stage: Stage) {
        super.start(stage)

        stage.minWidth = 800.0
        stage.minHeight = 700.0
    }
}