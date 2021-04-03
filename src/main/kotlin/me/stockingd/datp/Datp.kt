package me.stockingd.datp

class Datp {

    private val tokenizer = Tokenizer()
    private val parser = Parser()
    private val evaluator = Evaluator(
        null,
        createBindings(
            CoreModule(),
            NumberModule(),
        )
    )

    fun eval(program: String): String {
        return program.reader()
            .let { JavaReader(it) }
            .let { tokenizer.tokenize(it) }
            .let { TokenReader(it) }
            .let { parser.parse(it) }
            .let { evaluator.eval(it) }
            .let { it.print() }
    }

    private fun SExpr.print(): String {
        return when (this) {
            is SExpr.Atom.Number -> {
                if (value % 1 == 0.0) {
                    value.toLong().toString()
                } else {
                    value.toString()
                }
            }
            is SExpr.Atom.Str -> "\"$value\""
            is SExpr.Atom.Symbol -> value
            is SExpr.List -> "(${values.joinToString(separator = " ") { it.print() }})"
        }
    }
}
