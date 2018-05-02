package org.littlegit.core.model

import java.time.OffsetDateTime

data class RawCommit(
        val hash: String,
        val refs: List<String>,
        val parentHashes: List<String>,
        val date: OffsetDateTime,
        val committerEmail: String,
        val commitMessage: String,
        val isHead: Boolean
)