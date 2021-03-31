import me.stockingd.datp.Datp

fun main(args: Array<String>) {
    val datp = Datp()
    println(datp.eval("(define pi 3.14159)\n" +
            "(define radius 10)\n" +
            "(* pi (* radius radius))"))
}