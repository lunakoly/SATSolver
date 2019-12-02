package cdcl

import constructor.Literal
import general.AbstractSolver

/**
 * A solver that utilizes a dfs and
 * performs contradiction detection
 * via watching for each individual
 * literals occurrence in clauses
 * and learning contradicting literals
 */
class CDCLSolver : AbstractSolver<CDCLView> {
    override fun solve(view: CDCLView): Set<Literal>? {
        for (clause in view.clauses) {
            if (clause.literals.size == 1) {
                if (view.push(clause.literals.first(), clause) == CDCLView.BacktrackResult.UNSATISFIED) {
                    return null
                }
            }
        }

        while (view.hasNextLiteral()) {
            val next = view.getNextLiteral()

            if (view.push(next) == CDCLView.BacktrackResult.UNSATISFIED) {
                return null
            }
        }

        return view.exportSolution()
    }
}