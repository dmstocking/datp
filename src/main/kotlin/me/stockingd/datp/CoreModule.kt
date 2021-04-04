package me.stockingd.datp

class CoreModule : KotlinModule {

    override fun register(): Map<SExpr.Atom.Symbol, (Evaluator, List<SExpr>) -> SExpr> {
        return mapOf(
            SExpr.Atom.Symbol("quote") to quote(),
            SExpr.Atom.Symbol("eq") to eq(),
            SExpr.Atom.Symbol("=") to eq(),
        )
    }

    private fun quote(): (Evaluator, List<SExpr>) -> SExpr {
        return { _, args -> args.first() }
    }

    private fun eq(): (Evaluator, List<SExpr>) -> SExpr {
        return { evaluator, args ->
            args
                .map { evaluator.eval(it) }
                .windowed(2, 1)
                .fold(true) { eq, (a, b) -> eq && a == b }
                .let { if (it) SExpr.Atom.Symbol("true") else nil }
        }
    }
}