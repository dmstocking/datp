package me.stockingd.datp

class Evaluator {

    private val state = mutableMapOf<SExpr.Atom.Symbol, SExpr>()

    fun eval(expression: SExpr): SExpr {
        return when (expression) {
            is SExpr.Atom.Number -> expression
            is SExpr.Atom.Symbol -> state[expression]!!
            is SExpr.Atom.Str -> expression
            is SExpr.List -> {
                when (expression.values.first()) {
                    SExpr.Atom.Symbol("+") -> evaluateNumericOp(expression, Double::plus)
                    SExpr.Atom.Symbol("-") -> evaluateNumericOp(expression, Double::minus)
                    SExpr.Atom.Symbol("*") -> evaluateNumericOp(expression, Double::times)
                    SExpr.Atom.Symbol("/") -> evaluateNumericOp(expression, Double::div)
                    SExpr.Atom.Symbol("quote") -> expression.values.drop(1).first()
                    SExpr.Atom.Symbol("define") -> {
                        val (symbol, value) = expression
                            .values
                            .drop(1)
                            .take(2)
                        eval(value).also {
                            state[symbol as SExpr.Atom.Symbol] = it
                        }
                    }
                    else -> throw Exception("")
                }
            }
        }
    }

    private fun evaluateNumericOp(expression: SExpr.List, op: (Double, Double) -> Double): SExpr.Atom.Number {
        return expression
            .values
            .drop(1)
            .map { eval(it) }
            .map { (it as SExpr.Atom.Number).value }
            .reduce { acc, next -> op(acc, next) }
            .let { SExpr.Atom.Number(it) }
    }
}
