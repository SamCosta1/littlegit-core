package org.littlegit.core.model

import java.io.File

data class MergeResult(val conflictFiles: List<ConflictFile>) {
    val hasConflicts: Int; get() = conflictFiles.size
}

data class ConflictFile(val blobHash: String, val type: ConflictFileType, val file: File)

enum class ConflictFileType {
    Ours,
    Theirs,
    Base
}