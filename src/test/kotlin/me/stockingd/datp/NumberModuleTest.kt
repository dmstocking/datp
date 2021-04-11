package me.stockingd.datp

import io.kotest.inspectors.forAll
import io.kotest.matchers.shouldBe

internal class NumberModuleTest : DatpSpec({

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

    it("should evaluate less than") {
        listOf(
            "(lt 1 2)" to "true",
            "(lt 1 2 3)" to "true",
            "(lt 3 2)" to "nil",
            "(lt 3 3)" to "nil",
        ).forAll { (program, value) ->
            datp.eval(program).shouldBe(value)
        }
    }

    it("should evaluate less than or equal to") {
        listOf(
            "(le 1 2)" to "true",
            "(le 1 2 3)" to "true",
            "(le 3 2)" to "nil",
            "(le 3 3)" to "true",
        ).forAll { (program, value) ->
            datp.eval(program).shouldBe(value)
        }
    }

    it("should evaluate greater than") {
        listOf(
            "(gt 2 1)" to "true",
            "(gt 3 2 1)" to "true",
            "(gt 2 3)" to "nil",
            "(gt 3 3)" to "nil",
        ).forAll { (program, value) ->
            datp.eval(program).shouldBe(value)
        }
    }

    it("should evaluate greater than or equal to") {
        listOf(
            "(ge 2 1)" to "true",
            "(ge 3 2 1)" to "true",
            "(ge 2 3)" to "nil",
            "(ge 3 3)" to "true",
        ).forAll { (program, value) ->
            datp.eval(program).shouldBe(value)
        }
    }
})
