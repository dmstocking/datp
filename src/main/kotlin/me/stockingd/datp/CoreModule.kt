package me.stockingd.datp

class CoreModule : KotlinModule {

    override fun register(): Map<SExpr.Atom.Symbol, (Evaluator, List<SExpr>) -> SExpr> {
        return mapOf(
            SExpr.Atom.Symbol("quote") to quote(),
        )
    }

    private fun quote(): (Evaluator, List<SExpr>) -> SExpr {
        return { _, args -> args.first() }
    }
}