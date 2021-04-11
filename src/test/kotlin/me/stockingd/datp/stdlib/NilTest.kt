package me.stockingd.datp.stdlib

import io.kotest.assertions.assertSoftly
import io.kotest.matchers.shouldBe
import me.stockingd.datp.DatpSpec

class NilTest : DatpSpec({

    it("should reference nil to an empty list") {
        assertSoftly {
            // TODO: technically I think this just returns the Symbol nil so I need to evaluate it
            datp.eval("nil").shouldBe("nil")
            datp.eval("(cons 1 nil)").shouldBe("(1)")
        }
    }
})