import dfs.DFSSolver
import watchlist.WatchlistSolver

/**
 * Sugar helper for DFSSolver
 */
fun dfs(context: DFSSolver.() -> Unit) = DFSSolver().apply(context)

/**
 * Sugar helper for WatchlistSolver
 */
fun watchlist(context: WatchlistSolver.() -> Unit) = WatchlistSolver().apply(context)

/**
 * Sugar helper for the default solver
 */
fun sat(context: WatchlistSolver.() -> Unit) = watchlist(context)