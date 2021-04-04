package me.stockingd.datp

import kotlinx.collections.immutable.PersistentMap
import kotlinx.collections.immutable.persistentHashMapOf

typealias Bindings = PersistentMap<SExpr.Atom.Symbol, Binding>

sealed class Binding {
    data class Constant(val value: SExpr): Binding()
    data class Function(val params: SExpr.List, val implementation: SExpr): Binding()
    data class NativeFunction(val implementation: (Evaluator, List<SExpr>) -> SExpr): Binding()
}

fun createBindings(vararg modules: KotlinModule): Bindings {
    return modules.fold(persistentHashMapOf()) { bindings, module ->
        bindings.putAll(module.register().mapValues { (_, value) -> Binding.NativeFunction(value) })
    }
}