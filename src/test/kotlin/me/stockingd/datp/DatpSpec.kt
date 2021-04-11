package me.stockingd.datp

import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.DescribeSpec

@Suppress("UNCHECKED_CAST")
abstract class DatpSpec(
    spec: DatpSpec.() -> Unit
) : DescribeSpec({
    isolationMode = IsolationMode.InstancePerLeaf
    spec(this as DatpSpec)
}) {
    val datp = DatpFactory().create()
}