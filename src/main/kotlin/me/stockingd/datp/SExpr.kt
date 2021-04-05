package me.stockingd.datp

import kotlin.collections.List as KList

sealed class SExpr {
    data class List(val values: KList<SExpr>): SExpr()
    sealed class Atom: SExpr() {
        data class Str(val value: String): Atom()
        data class Number(val value: Double): Atom()
        data class Symbol(val value: String): Atom()
        data class Lambda(
            val parameters: KList<Symbol>,
            val implementation: KList<SExpr>
        ): Atom()
    }
}

val TRUE = SExpr.Atom.Symbol("true")
val NIL = SExpr.List(emptyList())