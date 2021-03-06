package me.stockingd.datp

import java.io.Reader

class TokenReader(val list: List<Token>) {

    var i = 0

    fun read(): Token? {
        return list.getOrNull(i++)
    }

    fun peek(): Token? {
        return list.getOrNull(i)
    }
}

inline fun <reified T : Token> TokenReader.readOrThrow(onError: (Token?) -> Throwable) {
    val token = read()
    if (token !is T) {
        throw onError(token)
    }
}

class JavaReader(val javaReader: Reader) {

    fun read(): Char? {
        return javaReader
            .read()
            .takeIf { it >= 0 }
            ?.toChar()
    }

    fun peek(): Char? {
        javaReader.mark(1)
        return read()
            .also { javaReader.reset() }
    }
}