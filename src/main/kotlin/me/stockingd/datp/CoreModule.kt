package me.stockingd.datp

class CoreModule : KotlinModule {

    override fun register(): Map<SExpr.Atom.Symbol, (Evaluator, List<SExpr>) -> SExpr> {
        return mapOf(
            SExpr.Atom.Symbol("lambda") to lambda(),
            SExpr.Atom.Symbol("apply") to apply(),
            SExpr.Atom.Symbol("quote") to quote(),
            SExpr.Atom.Symbol("eq") to eq(),
            SExpr.Atom.Symbol("=") to eq(),
            SExpr.Atom.Symbol("cond") to cond(),
        )
    }

    private fun lambda(): (Evaluator, List<SExpr>) -> SExpr {
        return { _, args ->
            val parameters = args
                .first()
                .let { it as SExpr.List }
                .values
                .map { it as SExpr.Atom.Symbol }
            val implementation = args.drop(1)
            SExpr.Atom.Lambda(parameters, implementation)
        }
    }

    private fun apply(): (Evaluator, List<SExpr>) -> SExpr {
        return { evaluator, args ->
            val (function, arguments) = args
            evaluator.eval(
                SExpr.List(
                    listOf(evaluator.eval(function)) + (evaluator.eval(arguments) as SExpr.List).values
                )
            )
        }
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
                .let { if (it) SExpr.Atom.Symbol("true") else NIL }
        }
    }

    // (cond
    //     ((eq x 1) 'true)
    //     ('true '())
    //     )
    private fun cond(): (Evaluator, List<SExpr>) -> SExpr {
        return { evaluator, args ->
            args
                .map { it as SExpr.List }
                .firstOrNull { sexpr ->
                    sexpr
                        .values
                        .first()
                        .let { evaluator.eval(it) }
                        .let { it == TRUE }
                }
                ?.values
                ?.let { (_, expression) -> evaluator.eval(expression) }
                ?: NIL
        }
    }
}