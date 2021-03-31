package me.stockingd.datp

import kotlinx.collections.immutable.persistentHashMapOf

class Datp {

    val tokenizer = Tokenizer()
    val parser = Parser()
    val evaluator = Evaluator(null, persistentHashMapOf())

    fun eval(program: String): SExpr {
        return program.reader()
            .let { JavaReader(it) }
            .let { tokenizer.tokenize(it) }
            .let { TokenReader(it) }
            .let { parser.parse(it) }
            .let { evaluator.eval(it) }
    }
}
