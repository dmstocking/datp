package me.stockingd.datp

import kotlinx.collections.immutable.PersistentMap


sealed class Binding {
    data class Constant(val value: SExpr): Binding()
    data class Function(val args: SExpr.List, val implementation: SExpr): Binding()
}
typealias Bindings = PersistentMap<SExpr.Atom.Symbol, Binding>

class Evaluator(private val parent: Evaluator?, private var bindings: Bindings) {

    fun eval(expression: SExpr): SExpr {

        return when (expression) {
            is SExpr.Atom.Number -> expression
            is SExpr.Atom.Symbol -> evalConstant(expression)
            is SExpr.Atom.Str -> expression
            is SExpr.List -> {
                when (expression.values.first()) {
                    SExpr.Atom.Symbol("+") -> evaluateNumericOp(expression, Double::plus)
                    SExpr.Atom.Symbol("-") -> evaluateNumericOp(expression, Double::minus)
                    SExpr.Atom.Symbol("*") -> evaluateNumericOp(expression, Double::times)
                    SExpr.Atom.Symbol("/") -> evaluateNumericOp(expression, Double::div)
                    SExpr.Atom.Symbol("quote") -> expression.values.drop(1).first()
                    SExpr.Atom.Symbol("define") -> {
                        val (sexpr, value) = expression
                            .values
                            .drop(1)
                            .take(2)
                        when (sexpr) {
                            is SExpr.List -> defineFunction(sexpr, value)
                            is SExpr.Atom.Symbol -> defineConstant(sexpr, value)
                            else -> throw Exception("")
                        }
                    }
                    else -> {
                        evalFunction(expression)
                    }
                }
            }
        }
    }

    private fun evalConstant(symbol: SExpr.Atom.Symbol): SExpr {
        return when (val result = bindings[symbol]) {
            is Binding.Constant -> result.value
            is Binding.Function -> throw Exception(
                "$symbol is defined as a function. Invoke it for a result."
            )
            null -> parent?.evalConstant(symbol)
        } ?: throw Exception("$symbol is not defined.")
    }

    private fun defineConstant(symbol: SExpr.Atom.Symbol, value: SExpr): SExpr {
        return eval(value).also {
            bindings = bindings.put(symbol, Binding.Constant(it))
        }
    }

    private fun evalFunction(expression: SExpr.List): SExpr {
        val sexpr = expression.values.first() as SExpr.Atom.Symbol
        val (args, impl) = getFunction(sexpr)
        val values = expression.values.drop(1).map { eval(it) }
        val arguments = args?.values?.map { it as SExpr.Atom.Symbol } ?: emptyList()
        var newScope = bindings
        values.zip(arguments) { value, arg ->
            newScope = newScope.put(arg, Binding.Function(SExpr.List(emptyList()), value))
        }
        return Evaluator(this, newScope).eval(impl)
    }

    fun getFunction(symbol: SExpr.Atom.Symbol): Binding.Function {
        return when (val result = bindings[symbol]) {
            is Binding.Constant ->
                throw Exception("$symbol is defined as a constant.")
            is Binding.Function -> result
            null -> parent?.getFunction(symbol)
        } ?: throw Exception("$symbol is not defined.")
    }

    private fun defineFunction(functionDefinition: SExpr.List, implementation: SExpr): SExpr {
        val symbol = functionDefinition.values.first() as SExpr.Atom.Symbol
        val args = functionDefinition
            .values
            .drop(1)
            .map { it as SExpr.Atom.Symbol }
            .let { SExpr.List(it) }
        bindings = bindings.put(symbol, Binding.Function(args, implementation))
        return functionDefinition
    }

    private fun evaluateNumericOp(
        expression: SExpr.List,
        op: (Double, Double) -> Double
    ): SExpr.Atom.Number {
        return expression
            .values
            .drop(1)
            .map { eval(it) }
            .map { (it as SExpr.Atom.Number).value }
            .reduce { acc, next -> op(acc, next) }
            .let { SExpr.Atom.Number(it) }
    }
}
