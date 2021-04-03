package me.stockingd.datp

import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.inspectors.forAll
import io.kotest.matchers.shouldBe

internal class NumberModuleTest : DescribeSpec({

    isolationMode = IsolationMode.InstancePerLeaf

    val datp = Datp()

    it("should sum all numbers") {
        listOf(
            "(+ 1 2)" to "3",
            "(+ 1 2 3 4)" to "10",
        ).forAll { (program, value) ->
            datp.eval(program).shouldBe(value)
        }
    }

    it("should subtract all numbers") {
        listOf(
            "(- 2 1)" to "1",
            "(- 50 10 20)" to "20",
        ).forAll { (program, value) ->
            datp.eval(program).shouldBe(value)
        }
    }

    it("should multiply all numbers") {
        listOf(
            "(* 1 2)" to "2",
            "(* 1 2 3 4)" to "24",
        ).forAll { (program, value) ->
            datp.eval(program).shouldBe(value)
        }
    }

    it("should divide all numbers") {
        listOf(
            "(/ 1 2)" to "0.5",
            "(/ 1 2 3)" to "0.16666666666666666",
        ).forAll { (program, value) ->
            datp.eval(program).shouldBe(value)
        }
    }
})
