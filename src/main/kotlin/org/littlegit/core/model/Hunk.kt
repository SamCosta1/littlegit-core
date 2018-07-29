package org.littlegit.core.model

typealias Patch = List<String>
data class Hunk(val fromStartLine: Int,
                val numFromLines: Int,
                val toStartLine: Int,
                val numToLines: Int,
                val hunkHeader: String,
                val lines: List<DiffLine>) {

    fun generatePatch(fileDiff: FileDiff): Patch {
        val patch = generatePatchHeaderLines(fileDiff)

        patch.add("@@ -$fromStartLine,$numFromLines +$toStartLine,$numToLines @@")

        lines.forEach {
            val lineStart = when(it.type) {
                DiffLineType.Addition -> '+'
                DiffLineType.Deletion -> '-'
                DiffLineType.Unchanged -> ' '
                DiffLineType.NoNewLineAtEndOfFile -> '/'
            }

            patch.add("$lineStart${it.line}")
        }

        return patch
    }

    private fun generatePatchHeaderLines(fileDiff: FileDiff): MutableList<String >{
        var fromPath = ""
        var toPath = ""

        when(fileDiff) {
            is SingleFilePathDiff -> {
                fromPath = fileDiff.filePath
                toPath = fileDiff.filePath
            }
            is FileDiff.RenamedFile -> {
                fromPath = fileDiff.originalPath
                toPath = fileDiff.newPath
            }
        }

        return mutableListOf(
            "diff --git a/$fromPath b/$toPath",
            "--- a/$fromPath",
            "+++ b/$toPath"
        )


    }

}