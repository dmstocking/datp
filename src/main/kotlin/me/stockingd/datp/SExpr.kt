package me.stockingd.datp

sealed class SExpr {
    data class List(val values: kotlin.collections.List<SExpr>): SExpr()
    sealed class Atom: SExpr() {
        data class Str(val value: String): Atom()
        data class Number(val value: Double): Atom()
        data class Symbol(val value: String): Atom()
    }
}

val TRUE = SExpr.Atom.Symbol("true")
val NIL = SExpr.List(emptyList())