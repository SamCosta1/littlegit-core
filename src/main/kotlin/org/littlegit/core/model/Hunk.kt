package org.littlegit.core.model

typealias Patch = List<String>
data class Hunk(val fromStartLine: Int,
                val numFromLines: Int,
                val toStartLine: Int,
                val numToLines: Int,
                val hunkHeader: String,
                val lines: List<DiffLine>) {

    // Only accept changed files. It doesn't makes sense to make patches for new, deleted or renamed files
    // since we only use this to generate patches for staging and unstaging hunks and for whole files patches aren't needed
    fun generatePatch(fileDiff: FileDiff.ChangedFile): Patch {
        val patch = generatePatchHeaderLines(fileDiff)

        patch.add("@@ -$fromStartLine,$numFromLines +$toStartLine,$numToLines @@")

        lines.forEach {
            val lineStart = getDiffLineSymbol(it.type)
            patch.add("$lineStart${it.line}")
        }

        return patch
    }

    private fun getDiffLineSymbol(it: DiffLineType) = when (it) {
        DiffLineType.Addition -> '+'
        DiffLineType.Deletion -> '-'
        DiffLineType.Unchanged -> ' '
        DiffLineType.NoNewLineAtEndOfFile -> '/'
    }


    // Generates a patch which undoes this hunk
    fun generateInversePatch(fileDiff: FileDiff.ChangedFile): Patch {
        val patch = generatePatchHeaderLines(fileDiff)

        patch.add("@@ -$toStartLine,$numToLines +$fromStartLine,$numFromLines @@")

        lines.forEach {
            val lineStart = getDiffLineSymbol(it.type.inverse)
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