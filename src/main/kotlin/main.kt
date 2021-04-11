import me.stockingd.datp.DatpFactory
import java.io.File

fun main(args: Array<String>) {
    val datp = DatpFactory().create()
    val file = args[0]
    File(file)
        .readText()
        .let { datp.eval(it) }
        .let { println(it) }
}