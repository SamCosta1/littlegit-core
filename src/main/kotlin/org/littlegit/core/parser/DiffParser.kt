package org.littlegit.core.parser

import org.littlegit.core.model.*
import org.littlegit.core.util.ListUtils
import org.littlegit.core.util.joinWithNewLines
import org.littlegit.core.util.joinWithSpace

class MalformedDiffException(override var message: String = "Diff is malformed", exception: Exception): Exception(message, exception)

object DiffParser {

    fun parse(lines: List<String>): Diff {
        val diffStartIndexes = ListUtils.findAllIndexesWhere(lines) { it.startsWith("diff --git") }
        val fileDiffs  = mutableListOf<FileDiff>()

        try {
            diffStartIndexes.forEachIndexed { indexOfIndex, _ ->
                val startIndex = diffStartIndexes[indexOfIndex] // The index of the diff start index in the diffStartIndexes list
                val endIndex = if (indexOfIndex == diffStartIndexes.lastIndex) {
                    lines.size
                } else {
                    diffStartIndexes[indexOfIndex + 1]
                }

                fileDiffs.add(parseFileDiff(lines, startIndex, endIndex))
            }
        } catch (e: Exception) {
            throw MalformedDiffException(exception = e)
        }

        return Diff(fileDiffs)
    }

    private fun parseFileDiff(lines: List<String>, startIndex: Int, endIndex: Int): FileDiff {
        /*

         Before the actual diff are the lines for the form
         --- a/src/main/kotlin/org/littlegit/core/parser/GitResultParser.kt
         +++ b/src/main/kotlin/org/littlegit/core/parser/GitResultParser.kt

         */
        val aFilePathIndex = ListUtils.firstOccurrenceAfterIndex(lines, startIndex) { it.startsWith("---") }
        val bFilePathIndex = aFilePathIndex + 1

        val hunks = mutableListOf<Hunk>()
        var currentHunk: Hunk? = null

        // These initial values shouldn't ever be used, will always be initialised at the start of each hunk
        var fromLineNumber: Int = -1
        var toLineNumber: Int = -1
        var currentDiffLines = mutableListOf<DiffLine>()
        for (index in (aFilePathIndex + 2) until endIndex) {
            val line = lines[index]

            // Hunk header line is in the form:   @@ from-fileDiffs-range to-fileDiffs-range @@ [header]
            if (line.startsWith("@@ ")) {
                if (currentHunk != null) hunks.add(currentHunk)

                val splitted = line.split(" ")
                val header = splitted.subList(4, splitted.size).joinWithSpace().trim()
                val fromFileRange = splitted[1].removePrefix("-").split(",").map { it.trim().toInt() }.toMutableList()
                val toFileRange = splitted[2].removePrefix("+").split(",").map { it.trim().toInt() }.toMutableList()

                if (fromFileRange.size < 2) fromFileRange.add(0)
                if (toFileRange.size < 2) toFileRange.add(0)

                currentDiffLines = mutableListOf()
                currentHunk = Hunk(fromFileRange.first(), fromFileRange.last(), toFileRange.first(), toFileRange.last(), header, currentDiffLines)
                fromLineNumber = currentHunk.fromStartLine
                toLineNumber = currentHunk.toStartLine
            } else {
                val diffLine: DiffLine = when {
                    line.startsWith("+") -> {
                        DiffLine(DiffLineType.Addition, toLineNum = toLineNumber++, line = line.removePrefix("+"))
                    }
                    line.startsWith("-") -> {
                        DiffLine(DiffLineType.Deletion, fromLineNum = fromLineNumber++, line = line.removePrefix("-"))
                    }
                    line.startsWith("\\ No newline at end of file") -> {
                        DiffLine(DiffLineType.NoNewLineAtEndOfFile, line = line)
                    }
                    else -> {
                        DiffLine(DiffLineType.Unchanged, fromLineNum = fromLineNumber++, toLineNum = toLineNumber++, line = line.removePrefix(" "))
                    }
                }

                currentDiffLines.add(diffLine)
            }
        }

        currentHunk?.let { hunks.add(it) }

        val aFilePath = lines[aFilePathIndex].removePrefix("--- ").removePrefix("a/")
        val bFilePath = lines[bFilePathIndex].removePrefix("+++ ").removePrefix("b/")

        return when {
            aFilePath == "/dev/null" -> NewFile(bFilePath, hunks)
            bFilePath == "/dev/null" -> DeletedFile(aFilePath, hunks)
            aFilePath != bFilePath -> RenamedFile(aFilePath, bFilePath, hunks)
            else -> ChangedFile(aFilePath, hunks)
        }
    }

    //private
}