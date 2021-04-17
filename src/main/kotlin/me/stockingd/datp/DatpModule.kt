package me.stockingd.datp

class DatpModule {

    private val files = listOf(
        "stdlib/nil.dapt",
        "stdlib/min-max.dapt",
    )

    fun load(datp: Datp) {
        files
            .asSequence()
            .map { this.javaClass.classLoader.getResourceAsStream(it)!! }
            .map { it.bufferedReader() }
            .forEach { datp.eval(it) }
    }
}