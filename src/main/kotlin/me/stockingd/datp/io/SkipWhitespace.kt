package me.stockingd.datp.io

import me.stockingd.datp.JavaReader

fun JavaReader.skipWhitespace() {
    generateSequence { this.peek() }
        .takeWhile { it.isWhitespace() }
        .forEach { _ -> this.read() }
}