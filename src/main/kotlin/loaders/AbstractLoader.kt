package loaders

import constructor.Formula
import constructor.Variable
import java.io.File

/**
 * Some tool to load formulas from a file
 */
interface AbstractLoader {
    /**
     * Loads a set of formulas from a file
     */
    fun load(file: File) : ArrayList<Pair<Formula, Map<Variable, String>>>
}