package org.littlegit.core.model

import java.io.File
import java.nio.file.Path

data class MergeResult(val conflictFiles: List<ConflictFile>) {
    val hasConflicts: Boolean; get() = !conflictFiles.isEmpty()
}

data class ConflictFile(val file: Path, val oursBlobHash: String, val theirsBlobHash: String, val baseBlobHash: String)
