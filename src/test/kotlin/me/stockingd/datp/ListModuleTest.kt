package me.stockingd.datp

import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.inspectors.forAll
import io.kotest.matchers.shouldBe

internal class ListModuleTest : DescribeSpec({

    isolationMode = IsolationMode.InstancePerLeaf

    val datp = Datp()

    describe("car") {
        it("should retrieve the first item in a list") {
            listOf(
                "(car (quote (1 2 3)))" to "1",
                "(car (quote ()))" to "nil",
            ).forAll { (program, value) ->
                datp.eval(program).shouldBe(value)
            }
        }
    }

    describe("cdr") {
        it("should retrieve everything but the first item in a list") {
            listOf(
                "(cdr (quote (1 2 3)))" to "(2 3)",
                "(cdr (quote (1 2)))" to "(2)",
                "(cdr (quote (1)))" to "nil",
                "(cdr (quote ()))" to "nil",
            ).forAll { (program, value) ->
                datp.eval(program).shouldBe(value)
            }
        }
    }
})