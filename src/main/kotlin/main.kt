import me.stockingd.datp.Datp
import java.io.File

fun main(args: Array<String>) {
    val datp = Datp()
    val file = args[0]
    File(file)
        .readText()
        .let { datp.eval(it) }
        .let { println(it) }
}