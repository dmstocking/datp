package me.stockingd.datp

interface KotlinModule {
    fun register(): Map<SExpr.Atom.Symbol, (Evaluator, List<SExpr>) -> SExpr>
}