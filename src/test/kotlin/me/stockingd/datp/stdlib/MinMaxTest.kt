package me.stockingd.datp.stdlib

import io.kotest.assertions.assertSoftly
import io.kotest.matchers.shouldBe
import me.stockingd.datp.DatpSpec

class MinMaxTest : DatpSpec({

    it("should return the min argument") {
        assertSoftly {
            datp.eval("(min 1 2)").shouldBe("1")
            datp.eval("(min 2 1)").shouldBe("1")
            datp.eval("(min 100 2)").shouldBe("2")
        }
    }

    it("should return the max argument") {
        assertSoftly {
            datp.eval("(max 1 2)").shouldBe("2")
            datp.eval("(max 2 1)").shouldBe("2")
            datp.eval("(max 100 2)").shouldBe("100")
        }
    }
})