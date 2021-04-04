package me.stockingd.datp

class Parser {

    fun parse(reader: TokenReader): SExpr {
        return when(val token = reader.read()) {
            Token.Open -> {
                generateSequence { reader.peek() }
                    .takeWhile { it != Token.Close }
                    .map { parse(reader) }
                    .toList()
                    .let { SExpr.List(it) }
                    .also { reader.readOrThrow<Token.Close> { Exception("Missing closing parenthesis. Found $it") } }
            }
            is Token.Symbol -> SExpr.Atom.Symbol(token.value)
            is Token.Number -> SExpr.Atom.Number(token.value)
            is Token.Str -> SExpr.Atom.Str(token.value)
            Token.Close -> throw Exception("Extra closing paran ')'")
            null -> throw Exception("Open paran '('")
        }
    }
}