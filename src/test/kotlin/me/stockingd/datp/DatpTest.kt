package me.stockingd.datp

import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.inspectors.forAll
import io.kotest.matchers.shouldBe

class DatpTest: DescribeSpec({

    isolationMode = IsolationMode.InstancePerLeaf

    val datp = Datp()

    it("should support parsing all digits") {
        (0..9).toList().forAll { digit ->
            datp.eval("$digit").shouldBe(digit.toString())
        }
    }

    it("should evaluate inner functions") {
        listOf(
            "(+ 1 (+ 1 (+ 1)))" to "3",
            "(/ (* 420 420) 420)" to "420",
        ).forAll { (program, value) ->
            datp.eval(program).shouldBe(value)
        }
    }

    it("should save state and recall it") {
        listOf(
            "(define pi 3.14159)" to "3.14159",
            "(define radius 10)" to "10",
            "(* pi (* radius radius))" to "314.159",
            "(define circumference (* 2 pi radius))" to "62.8318",
        )
            .map { (prog, value) -> datp.eval(prog).shouldBe(value) }
    }

    it("should execute functions") {
        listOf(
            "(define (square x) (* x x))" to "(square x)",
            "(define (double x) (+ x x))" to "(double x)",
            "(square (+ 1 2 3))" to "36",
            "(square (square 2))" to "16",
            "(square (double 5))" to "100",
        )
            .map { (prog, value) -> datp.eval(prog).shouldBe(value) }
    }
})