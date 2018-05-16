package org.littlegit.core.model
import java.time.OffsetDateTime

class FullCommit(hash: String,
                 refs: List<String>,
                 parentHashes: List<String>,
                 date: OffsetDateTime,
                 committerEmail: String,
                 commitSubject: String,
                 isHead: Boolean,
                 val commitBody: List<String>)
    : RawCommit(hash,
        refs,
        parentHashes,
        date,
        committerEmail,
        commitSubject,
        isHead) {


    companion object {
        fun from(raw: RawCommit, commitBody: List<String>): FullCommit {
            return FullCommit(raw.hash, raw.refs, raw.parentHashes, raw.date, raw.committerEmail, raw.commitSubject, raw.isHead, commitBody)
        }
    }
}
