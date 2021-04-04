package me.stockingd.datp

import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.inspectors.forAll
import io.kotest.matchers.shouldBe

internal class CoreModuleTest : DescribeSpec({

    isolationMode = IsolationMode.InstancePerLeaf

    val datp = Datp()

    it("should evaluate quote to the exact data in it") {
        listOf(
            "(quote 1)" to "1",
            "(quote (1 2 3 4))" to "(1 2 3 4)",
            "(quote (1 2 (a b) 4))" to "(1 2 (a b) 4)",
        ).forAll { (program, value) ->
            datp.eval(program).shouldBe(value)
        }
    }

    it("should check if things are equal") {
        listOf(
            "(eq 1 1)" to "true",
            "(eq (quote true) (quote true))" to "true",
            "(eq (quote true) (quote ()))" to "nil",
            "(eq (quote (1 2 3)) (quote (1 2 3)))" to "true",
            "(eq (quote (1 2 3 4)) (quote (1 2 3)))" to "nil",
        ).forAll { (program, value) ->
            datp.eval(program).shouldBe(value)
        }
    }
})