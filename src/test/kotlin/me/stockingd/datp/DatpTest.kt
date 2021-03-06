package me.stockingd.datp

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.inspectors.forAll
import io.kotest.matchers.shouldBe

class DatpTest: DatpSpec({

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

    it("should define multi expression functions") {
        datp.eval("""
            (define (waste x) (+ x x) (* x x))
            (waste 2)
        """).shouldBe("4")
    }

    it("should define a function in a function") {
        datp.eval("""
            (define (square-double x)
                (define (square x) (* x x))
                (define (double x) (+ x x))
                (square (double x))
                )
            (square-double 2)
        """).shouldBe("16")
    }

    it("should not allow inner defined functions to escape") {
        shouldThrow<Exception> {
            datp.eval("""
            (define (square-double x)
                (define (square x) (* x x))
                (define (double x) (+ x x))
                (square (double x))
                )
            (square 2)
        """)
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

    it("should support ' as a sub for (quote ...) function") {
        listOf(
            "'(1 2 3)" to "(1 2 3)",
            "'asdf" to "asdf",
            "'(max 1 2 (a b))" to "(max 1 2 (a b))",
            "'1234" to "1234"
        )
            .map { (prog, value) -> datp.eval(prog).shouldBe(value) }
    }
})