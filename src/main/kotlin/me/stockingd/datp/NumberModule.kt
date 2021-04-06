package me.stockingd.datp

class NumberModule: KotlinModule {

    override fun register(): Map<SExpr.Atom.Symbol, (Evaluator, List<SExpr>) -> SExpr> {
        return mapOf(
            SExpr.Atom.Symbol("plus") to evaluateNumericOp(Double::plus),
            SExpr.Atom.Symbol("+") to evaluateNumericOp(Double::plus),
            SExpr.Atom.Symbol("minus") to evaluateNumericOp(Double::minus),
            SExpr.Atom.Symbol("-") to evaluateNumericOp(Double::minus),
            SExpr.Atom.Symbol("times") to evaluateNumericOp(Double::times),
            SExpr.Atom.Symbol("*") to evaluateNumericOp(Double::times),
            SExpr.Atom.Symbol("div") to evaluateNumericOp(Double::div),
            SExpr.Atom.Symbol("/") to evaluateNumericOp(Double::div),
            SExpr.Atom.Symbol("rem") to evaluateNumericOp(Double::rem),
            SExpr.Atom.Symbol("%") to evaluateNumericOp(Double::rem),
            SExpr.Atom.Symbol("lt") to evaluateCompareOp { a, b -> a < b },
            SExpr.Atom.Symbol("le") to evaluateCompareOp { a, b -> a <= b },
            SExpr.Atom.Symbol("gt") to evaluateCompareOp { a, b -> a > b },
            SExpr.Atom.Symbol("ge") to evaluateCompareOp { a, b -> a >= b },
        )
    }

    private fun evaluateNumericOp(
        op: (Double, Double) -> Double
    ): (Evaluator, List<SExpr>) -> SExpr {
        return { evaluator, args ->
            args
                .map { evaluator.eval(it) }
                .map { (it as SExpr.Atom.Number).value }
                .reduce { acc, next -> op(acc, next) }
                .let { SExpr.Atom.Number(it) }
        }
    }

    private fun evaluateCompareOp(
        op: (Double, Double) -> Boolean
    ): (Evaluator, List<SExpr>) -> SExpr {
        return { evaluator, args ->
            args.map { evaluator.eval(it) as SExpr.Atom.Number }
                .map { it.value }
                .windowed(2, 1)
                .fold(true) { result, (a, b) -> result && op(a, b) }
                .let { if (it) SExpr.Atom.Symbol("true") else NIL }
        }
    }
}