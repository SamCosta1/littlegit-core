package org.littlegit.core.model

data class Diff(val fileDiffs: List<FileDiff>)

interface FileDiff {
    val hunks: List<Hunk>

    data class NewFile(val filePath: String, override val hunks: List<Hunk>) : FileDiff
    data class DeletedFile(val filePath: String, override val hunks: List<Hunk>) : FileDiff
    data class RenamedFile(val originalPath: String, val newPath: String, override val hunks: List<Hunk>) : FileDiff
    data class ChangedFile(val filePath: String, override val hunks: List<Hunk>) : FileDiff
}

