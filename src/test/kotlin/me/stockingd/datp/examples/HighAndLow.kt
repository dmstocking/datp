package me.stockingd.datp.examples

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import me.stockingd.datp.DatpFactory

class HighAndLow : DescribeSpec({

    val datp = DatpFactory().create()

    it("should find the highest and lowest in the list") {
        """
(define (high-and-low list)
    (cond
        ((eq nil list) (quote (nil, nil)))
        ((quote true) (high-and-low (car list) (car list) (cdr list)))
    )
)

(define (high-and-low-impl low high list)
    (cond
        ((eq nil list) (high-and-low-impl (min low (car list)) (max high (car list)) (cdr list)))
        ((quote true) (cons low (cons high (quote ()))))
    )
)
        
(high-and-low '(1 9 3 5 2 0))
"""
            .let { datp.eval(it) }
            .shouldBe("(0 9)")
    }
})