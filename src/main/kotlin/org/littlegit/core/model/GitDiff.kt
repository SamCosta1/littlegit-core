package org.littlegit.core.model

data class Diff(val fileDiffs: List<FileDiff>)

interface SingleFilePathDiff: FileDiff {
    val filePath: String
}

interface FileDiff {
    val hunks: List<Hunk>

    data class NewFile(override val filePath: String, override val hunks: List<Hunk>) : SingleFilePathDiff
    data class DeletedFile(override val filePath: String, override val hunks: List<Hunk>) : SingleFilePathDiff
    data class RenamedFile(val originalPath: String, val newPath: String, override val hunks: List<Hunk>) : FileDiff
    data class ChangedFile(override val filePath: String, override val hunks: List<Hunk>) : SingleFilePathDiff
}

