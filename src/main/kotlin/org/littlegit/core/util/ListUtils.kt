package org.littlegit.core.util

import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import java.util.stream.Collectors
import java.io.IOException
import java.util.stream.Stream



fun List<String>.joinWithNewLines(): String {
    return this.joinToString(System.lineSeparator())
}

fun List<String>.joinWithSpace(): String {
    return this.joinToString(" ")
}

object ListUtils {
    fun <T>findAllIndexesWhere(list: List<T>, predicate: (T) -> Boolean): List<Int> {
        val indexes = mutableListOf<Int>()

        list.forEachIndexed { index, item ->
            if (predicate(item)) {
                indexes.add(index)
            }
        }

        return indexes
    }

    fun <T>firstOccurrenceAfterIndex(list: List<T>, index: Int, predicate: (T) -> Boolean): Int {
        for (i in index until list.size)
            if (predicate(list[i]))
                return i

        return -1;
    }

    fun writeToFile(message: List<String>, tempFile: File?) {
        tempFile?.bufferedWriter().use { out ->
            message.forEach {
                out?.write(it)
                out?.newLine()
            }
        }
    }

    fun readFromPath(path: Path) = path.toFile().readLines()
}