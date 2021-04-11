package me.stockingd.datp

import io.kotest.core.spec.IsolationMode
import io.kotest.inspectors.forAll
import io.kotest.matchers.shouldBe

internal class CoreModuleTest : DatpSpec({

    it("should create lambdas") {
        datp.eval("(lambda (x) (+ x x))")
            .shouldBe("(LAMBDA (x) (+ x x))")
    }

    it("should apply arguments to function") {
        listOf(
            "(apply (quote +) (quote (1 2)))" to "3",
        ).forAll { (program, value) ->
            datp.eval(program).shouldBe(value)
        }
    }

    it("should apply lambdas") {
        datp.eval("(apply (lambda (x) (+ x x)) (quote (1)))")
            .shouldBe("2")
    }

    it("should apply lambdas from defines") {
        datp.eval("""
            (define l (lambda (x) (+ x x)))
            (apply l (quote (1)))
        """).shouldBe("2")
    }

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

    it("should evaluate conditional statements") {
        listOf(
            "(cond ((quote true) 1))" to "1",
            "(cond ((eq 1 0) 1) ((eq 1 1) 7))" to "7",
            "(cond ((eq 1 0) 1))" to "nil",
        ).forAll { (program, value) ->
            datp.eval(program).shouldBe(value)
        }
    }

    it("should tell if something is an atom or not") {
        listOf(
            "(atom? 1234)" to "true",
            "(atom? \"asdf\")" to "true",
            "(atom? (quote ()))" to "true",
            "(atom? (quote (1 2 3)))" to "nil",
        ).forAll { (program, value) ->
            datp.eval(program).shouldBe(value)
        }
    }

    it("should tell if something is a list or not") {
        listOf(
            "(list? 1234)" to "nil",
            "(list? \"asdf\")" to "nil",
            "(list? (quote ()))" to "nil",
            "(list? (quote (1 2 3)))" to "true",
        ).forAll { (program, value) ->
            datp.eval(program).shouldBe(value)
        }
    }
})