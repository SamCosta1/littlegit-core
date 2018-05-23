package org.littlegit.core.parser

import org.littlegit.core.model.Diff
import org.littlegit.core.model.FileDiff
import org.littlegit.core.util.ListUtils

object DiffParser {

    fun parse(lines: List<String>): Diff {
        val diffStartIndexes = ListUtils.findAllIndexesWhere(lines) { it.startsWith("diff --git") }
        val fileDiffs  = mutableListOf<FileDiff>()

        diffStartIndexes.forEachIndexed { indexOfIndex, _ ->
            val startIndex = diffStartIndexes[indexOfIndex] // The index of the diff start index in the diffStartIndexes list
            val endIndex = if (indexOfIndex == diffStartIndexes.lastIndex) {
                diffStartIndexes.size
            } else {
                diffStartIndexes[indexOfIndex + 1]
            }

            fileDiffs.add(parseFileDiff(lines, startIndex, endIndex))
        }

        return Diff(fileDiffs)
    }

    private fun parseFileDiff(lines: List<String>, startIndex: Int, endIndex: Int): FileDiff {
        return FileDiff()
    }
}