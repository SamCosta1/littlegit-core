package org.littlegit.core.model

import java.nio.file.Path

data class MergeResult(val conflictFiles: List<ConflictFile>) {
    val hasConflicts: Boolean; get() = !conflictFiles.isEmpty()
}

data class ConflictFile(val filePath: Path, val oursBlobHash: String, val theirsBlobHash: String, val baseBlobHash: String)
