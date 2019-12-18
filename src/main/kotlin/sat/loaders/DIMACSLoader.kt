package sat.loaders

import sat.constructor.Clause
import sat.constructor.Formula
import sat.constructor.Variable
import java.io.File
import kotlin.math.abs

/**
 * Loads formulas from DIMACS files
 */
object DIMACSLoader : AbstractLoader {
    /**
     * Loads a single cnf problem from an opened file
     */
    private fun loadCNF(
        numberOfVariables: Int,
        numberOfClauses: Int,
        source: LineIterator
    ): Pair<Formula, Map<Variable, String>> {
        // We need to map numbers into
        // already defined variables. If there's
        // no already defined variable for a number
        // we'll return 'undefined' variable
        val undefined = Variable()
        val variables = Array(numberOfVariables) { undefined }

        val result = Formula()
        var currentClause = Clause()
        var it = 0

        while (it < numberOfClauses) {
            val parts = source.next()
                .split("\\s+".toRegex())
                .filter { it.isNotEmpty() }

            for (each in parts) {
                // require int
                val literal = each.toIntOrNull()
                    ?: throw source.raise("Literals must be defined as integers")

                // terminator
                if (literal == 0) {
                    result.clauses.add(currentClause)
                    currentClause = Clause()
                    it += 1
                } else {
                    val index = abs(literal) - 1

                    if (variables[index] == undefined) {
                        variables[index] = Variable()
                    }

                    if (literal > 0) {
                        currentClause.literals.add(+variables[index])
                    } else {
                        currentClause.literals.add(-variables[index])
                    }
                }
            }
        }

        // to be able to turn the formula back
        // into something human-readable.
        // debug only
        val mapping = mutableMapOf<Variable, String>()

        for (index in variables.indices) {
            mapping[variables[index]] = (index + 1).toString()
        }

        return result to mapping
    }

    /**
     * Loads a single problem from an opened file
     */
    private fun loadProblem(source: LineIterator): Pair<Formula, Map<Variable, String>> {
        val parts = source.last.split("\\s+".toRegex())

        source.require(parts.size > 1) {
            "Instruction 'p' requires at leas one argument"
        }

        when (parts[1]) {
            "cnf" -> {
                source.require(parts.size >= 4) {
                    "'cnf' form requires specifying the number of variables and the number clauses"
                }

                // unused numberOfVariables
                val numberOfVariables = parts[2].toIntOrNull()
                    ?: throw source.raise("Number of variables must be an integer")
                val numberOfClauses = parts[3].toIntOrNull()
                    ?: throw source.raise("Number of clauses must be an integer")

                return loadCNF(numberOfVariables, numberOfClauses, source)
            }

            else -> {
                throw source.raise("Incorrect type: '${parts[1]}'")
            }
        }
    }

    /**
     * Loads all problems from a LineNumber source
     */
    private fun loadProblems(source: LineIterator): Pair<Formula, Map<Variable, String>> {
        while (source.hasNext()) {
            val header = source.next()

            if (header.isNotEmpty()) {
                when (header.first()) {
                    'c' -> { /* comment */ }

                    'p' -> {
                        return loadProblem(source)
                    }

                    '%' -> {
                        // I have no idea what it means but
                        // it is present in benchmark files...
                    }

                    '0' -> {
                        // same as above.
                        // maybe they mean the end of the file?
                    }

                    else -> {
                        throw source.raise("Incorrect format: '${header}'")
                    }
                }
            }
        }

        throw source.raise("No formulas found!")
    }

    /**
     * An easier alternative to Iterator<IndexedValue<String>>
     */
    class LineIterator(
        /**
         * The underlying source
         */
        private val backend: Iterator<String>
    ) : Iterator<String> {
        /**
         * Tracks the line number
         */
        private var lineNumber: Int = 0

        /**
         * Saves the last line
         */
        private var lastBackend: String? = null

        /**
         * Returns the last line if present or
         * throws an Exception
         */
        val last: String
            get() = lastBackend ?: throw Exception("No previous lines!")

        override fun hasNext(): Boolean {
            return backend.hasNext()
        }

        override fun next(): String {
            val last = backend.next()
            lastBackend = last
            lineNumber += 1
            return last
        }

        /**
         * Builds an error message with line number
         * intro
         */
        private fun alert(message: String) = "Error at line $lineNumber: $message"

        /**
         * Returns an exception with the current line number
         */
        fun raise(message: String) = Exception(alert(message))

        /**
         * Throws an exception with the given message
         * if the condition fails
         */
        fun require(condition: Boolean, message: () -> Any) {
            if (!condition) {
                throw Exception(alert(message().toString()))
            }
        }
    }

    override fun load(file: File): Pair<Formula, Map<Variable, String>> {
        return file.useLines {
            // withIndex turned out to be inconvenient.
            // the code looks 'dirty' and 'difficult'
            loadProblems(LineIterator(it.iterator()))
        }
    }
}