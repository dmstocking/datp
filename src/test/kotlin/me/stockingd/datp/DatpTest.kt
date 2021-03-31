package me.stockingd.datp

import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.inspectors.forAll
import io.kotest.matchers.shouldBe

class DatpTest: DescribeSpec({

    isolationMode = IsolationMode.InstancePerLeaf

    val datp = Datp()

    it("should sum all numbers") {
        listOf(
            "(+ 1 2)" to 3.0,
            "(+ 1 2 3 4)" to 10.0,
        ).forAll { (program, value) ->
            datp.eval(program).shouldBe(SExpr.Atom.Number(value))
        }
    }

    it("should subtract all numbers") {
        listOf(
            "(- 2 1)" to 1.0,
            "(- 50 10 20)" to 20.0,
        ).forAll { (program, value) ->
            datp.eval(program).shouldBe(SExpr.Atom.Number(value))
        }
    }

    it("should multiply all numbers") {
        listOf(
            "(* 1 2)" to 2.0,
            "(* 1 2 3 4)" to 24.0,
        ).forAll { (program, value) ->
            datp.eval(program).shouldBe(SExpr.Atom.Number(value))
        }
    }

    it("should divide all numbers") {
        listOf(
            "(/ 1 2)" to 0.5,
            "(/ 1 2 3)" to 0.16666666666666666,
        ).forAll { (program, value) ->
            datp.eval(program).shouldBe(SExpr.Atom.Number(value))
        }
    }

    it("should evaluate inner functions") {
        listOf(
            "(+ 1 (+ 1 (+ 1)))" to 3.0,
            "(/ (* 420 420) 420)" to 420.0,
        ).forAll { (program, value) ->
            datp.eval(program).shouldBe(SExpr.Atom.Number(value))
        }
    }

    it("should evaluate quote to the exact data in it") {
        listOf(
            "(quote 1)" to SExpr.Atom.Number(1.0),
            "(quote (1 2 3 4))" to SExpr.List(listOf(
                SExpr.Atom.Number(1.0),
                SExpr.Atom.Number(2.0),
                SExpr.Atom.Number(3.0),
                SExpr.Atom.Number(4.0)
            )),
            "(quote (1 2 (a b) 4))" to SExpr.List(listOf(
                SExpr.Atom.Number(1.0),
                SExpr.Atom.Number(2.0),
                SExpr.List(listOf(
                    SExpr.Atom.Symbol("a"),
                    SExpr.Atom.Symbol("b")
                )),
                SExpr.Atom.Number(4.0)
            )),
        ).forAll { (program, value) ->
            datp.eval(program).shouldBe(value)
        }
    }

    it("should save state and recall it") {
        listOf(
            "(define pi 3.14159)" to SExpr.Atom.Number(3.14159),
            "(define radius 10)" to SExpr.Atom.Number(10.0),
            "(* pi (* radius radius))" to SExpr.Atom.Number(314.159),
            "(define circumference (* 2 pi radius))" to SExpr.Atom.Number(62.8318)
        )
            .map { (prog, value) -> datp.eval(prog).shouldBe(value) }
    }

    it("should execute functions") {
        listOf(
            "(define (square x) (* x x))" to SExpr.List(listOf(
                SExpr.Atom.Symbol("square"),
                SExpr.Atom.Symbol("x")
            )),
            "(define (double x) (+ x x))" to SExpr.List(listOf(
                SExpr.Atom.Symbol("double"),
                SExpr.Atom.Symbol("x")
            )),
            "(square (+ 1 2 3))" to SExpr.Atom.Number(36.0),
            "(square (square 2))" to SExpr.Atom.Number(16.0),
            "(square (double 5))" to SExpr.Atom.Number(100.0),
        )
            .map { (prog, value) -> datp.eval(prog).shouldBe(value) }
    }
})