package org.littlegit.core.model

data class Diff(val files: List<FileDiff>)

sealed class FileDiff(val hunks: List<Hunk>)

class NewFile(val filePath: String, hunk: List<Hunk>) : FileDiff(hunk)
class DeletedFile(val filePath: String, hunk: List<Hunk>) : FileDiff(hunk)
class RenamedFile(val originalPath: String, val newPath: String, hunk: List<Hunk>) : FileDiff(hunk)
class ChangedFile(val filePath: String, hunks: List<Hunk>): FileDiff(hunks)