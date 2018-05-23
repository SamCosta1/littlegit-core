package org.littlegit.core.model
import org.littlegit.core.util.joinWithNewLines
import java.time.OffsetDateTime

class FullCommit(hash: String,
                 refs: List<String>,
                 parentHashes: List<String>,
                 date: OffsetDateTime,
                 committerEmail: String,
                 commitSubject: String,
                 isHead: Boolean,
                 val commitBody: String)
    : RawCommit(hash,
        refs,
        parentHashes,
        date,
        committerEmail,
        commitSubject,
        isHead) {


    companion object {
        fun from(raw: RawCommit, commitBody: List<String>): FullCommit {
            return FullCommit(raw.hash, raw.refs, raw.parentHashes, raw.date, raw.committerEmail, raw.commitSubject, raw.isHead, commitBody.joinWithNewLines())
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        if (!super.equals(other)) return false

        other as FullCommit

        if (commitBody != other.commitBody) return false

        return true
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + commitBody.hashCode()
        return result
    }
}
