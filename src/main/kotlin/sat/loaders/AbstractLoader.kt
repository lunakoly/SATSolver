package sat.loaders

import sat.constructor.Formula
import sat.constructor.Variable
import java.io.File

/**
 * Some tool to load formulas from a file
 */
interface AbstractLoader {
    /**
     * Loads a formula from a file
     */
    fun load(file: File) : Pair<Formula, Map<Variable, String>>
}