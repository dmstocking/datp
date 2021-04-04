package me.stockingd.datp

class ListModule : KotlinModule {
    override fun register(): Map<SExpr.Atom.Symbol, (Evaluator, List<SExpr>) -> SExpr> {
        return mapOf(
            SExpr.Atom.Symbol("car") to car(),
            SExpr.Atom.Symbol("cdr") to cdr(),
        )
    }

    private fun car(): (Evaluator, List<SExpr>) -> SExpr {
        return { evaluator, args ->
            args
                .first()
                .let { evaluator.eval(it) }
                .let { it as SExpr.List }
                .values
                .firstOrNull()
                ?: NIL
        }
    }

    private fun cdr(): (Evaluator, List<SExpr>) -> SExpr {
        return { evaluator, args ->
            args
                .first()
                .let { evaluator.eval(it) }
                .let { it as SExpr.List }
                .values
                .drop(1)
                .let { SExpr.List(it) }
        }
    }
}