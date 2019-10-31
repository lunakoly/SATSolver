import dfs.DFSSolver

/**
 * Sugar helper for DFSSolver
 */
fun dfs(context: DFSSolver.() -> Unit) = DFSSolver().apply(context)

/**
 * Sugar helper for the default solver
 */
fun sat(context: DFSSolver.() -> Unit) = dfs(context)