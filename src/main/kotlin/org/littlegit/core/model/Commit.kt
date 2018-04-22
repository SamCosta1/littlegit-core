package org.littlegit.core.model

import java.time.OffsetDateTime

data class Commit(
        val hash: String,
        val parentHashes: List<String>,
        val refs: List<String>,
        val date: OffsetDateTime,
        val committerEmail: String,
        val commitMessage: String,
        val isHead: Boolean
)