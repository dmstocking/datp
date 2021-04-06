package me.stockingd.datp

class ListModule : KotlinModule {
    override fun register(): Map<SExpr.Atom.Symbol, (Evaluator, List<SExpr>) -> SExpr> {
        return mapOf(
            SExpr.Atom.Symbol("car") to car(),
            SExpr.Atom.Symbol("cdr") to cdr(),
            SExpr.Atom.Symbol("cons") to cons(),
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

    private fun cons(): (Evaluator, List<SExpr>) -> SExpr {
        return { evaluator, args ->
            val (first, second) = args.take(2).map { evaluator.eval(it) }
            if (second is SExpr.List) {
                SExpr.List(listOf(first) + second.values)
            } else {
                SExpr.List(listOf(first, second))
            }
        }
    }
}