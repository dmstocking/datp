package me.stockingd.datp

class Evaluator(private val parent: Evaluator?, private var bindings: Bindings) {

    fun eval(expression: SExpr): SExpr {

        return when (expression) {
            is SExpr.Atom.Number -> expression
            is SExpr.Atom.Symbol -> evalConstant(expression)
            is SExpr.Atom.Str -> expression
            is SExpr.List -> {
                when (expression.values.first()) {
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
            is Binding.Function, is Binding.NativeFunction -> throw Exception(
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
        return when (val function = getFunction(sexpr)) {
            is Binding.Constant -> throw Exception("Fetched function is not a function. Please report an issue.")
            is Binding.Function -> {
                val (args, impl) = function
                val values = expression.values.drop(1).map { eval(it) }
                val arguments = args.values.map { it as SExpr.Atom.Symbol }
                var newScope = bindings
                values.zip(arguments) { value, arg ->
                    newScope = newScope.put(arg, Binding.Constant(value))
                }
                Evaluator(this, newScope).eval(impl)
            }
            is Binding.NativeFunction -> {
                function.implementation(this, expression.values.drop(1))
            }
        }
    }

    fun getFunction(symbol: SExpr.Atom.Symbol): Binding {
        return when (val result = bindings[symbol]) {
            is Binding.Constant ->
                throw Exception("$symbol is defined as a constant.")
            is Binding.Function, is Binding.NativeFunction -> result
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
}
