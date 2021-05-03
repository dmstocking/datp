package me.stockingd.datp.examples

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import me.stockingd.datp.DatpFactory

class GenerateRange : DescribeSpec({

    val datp = DatpFactory().create()

    it("should recursively generate a range") {
        """
(define (generateRange min max step)
    (cond
        ((le min max) (cons min (generateRange (+ min step) max step)))
        ((quote true) (quote ()))
    )
)

(generateRange 2 10 2)
"""
            .let { datp.eval(it) }
            .shouldBe("(2 4 6 8 10)")
    }
})