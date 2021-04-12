package me.stockingd.datp

import me.stockingd.datp.io.skipWhitespace

sealed class Token {
    object Open: Token()
    class Symbol(val value: String): Token()
    class Str(val value: String): Token()
    class Number(val value: Double): Token()
    object Close: Token()
    object Quote: Token()
}

class Tokenizer {

    fun tokenize(reader: JavaReader): List<Token> {
        return generateSequence { readToken(reader) }
            .toList()
    }

    private fun readToken(reader: JavaReader): Token? {
        reader.skipWhitespace()
        return when (val c = reader.read()) {
            '(' -> Token.Open
            ')' -> Token.Close
            '\'' -> Token.Quote
            '"' -> {
                generateSequence { reader.peek() }
                    .takeWhile { it != '"' }
                    .map { reader.read() }
                    .let { sequenceOf(c) + it }
                    .joinToString(separator = "")
                    .let { Token.Str(it) }
                    .also { reader.read() }
            }
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', -> {
                generateSequence { reader.peek() }
                    .takeWhile { it.isDigit() || it == '.' }
                    .map { reader.read() }
                    .let { sequenceOf(c) + it }
                    .joinToString(separator = "")
                    .toDouble()
                    .let { Token.Number(it) }
            }
            null -> null
            else -> {
                generateSequence { reader.peek() }
                    .takeWhile { !it.isWhitespace() && it != '(' && it != ')' }
                    .map { reader.read() }
                    .let { sequenceOf(c) + it }
                    .joinToString(separator = "")
                    .let { Token.Symbol(it) }
            }
        }
    }

}

