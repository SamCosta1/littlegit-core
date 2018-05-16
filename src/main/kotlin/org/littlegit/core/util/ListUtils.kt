package org.littlegit.core.util

fun List<String>.joinWithNewLines(): String {
    return this.joinToString(System.lineSeparator())
}

fun List<String>.joinWithSpace(): String {
    return this.joinToString(" ")
}