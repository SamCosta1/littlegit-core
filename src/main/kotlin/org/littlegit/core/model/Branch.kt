package org.littlegit.core.model

import org.littlegit.core.commandrunner.CommitHash

abstract class Branch(
        val fullRefName: String,
        val isHead: Boolean,
        val commitHash: CommitHash?
) {
    companion object {
        fun createFrom(refName: String, head: Boolean, objectName: String, objectType: String, upstream: RemoteBranch? = null): Branch? {
            if (refName.isBlank() || objectName.isBlank()) {
                return null
            }

            val commitHash = if (ObjectType.fromRaw(objectType) == ObjectType.Commit) {
                objectName.trim()
            } else {
                null
            }

            return when {
                refName.startsWith("refs/heads/") -> LocalBranch(refName, head, upstream, commitHash)
                refName.startsWith("refs/remotes/") -> RemoteBranch(refName, head, commitHash)
                else -> null
            }
        }
    }


    abstract val branchName: String

    override fun equals(other: Any?): Boolean {
        return other is Branch
                && other.fullRefName == this.fullRefName
                && other.isHead == this.isHead
                && other.commitHash == this.commitHash
    }

    override fun hashCode(): Int {
        var result = fullRefName.hashCode()
        result = 31 * result + (commitHash?.hashCode() ?: 0)
        return result
    }
}

class RemoteBranch(refName: String, head: Boolean, commitHash: String?) : Branch(refName, head, commitHash) {
    override val branchName: String; get() = fullRefName.removePrefix("refs/remotes/")

    override fun equals(other: Any?): Boolean {
        return super.equals(other) && other is RemoteBranch
    }

}

class LocalBranch(refName: String, head: Boolean, val upstream: RemoteBranch?, commitHash: String?) : Branch(refName, head, commitHash) {
    override val branchName: String; get() = fullRefName.removePrefix("refs/heads/")

    override fun equals(other: Any?): Boolean {
        return super.equals(other) && other is LocalBranch && this.upstream == other.upstream
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + (upstream?.hashCode() ?: 0)
        return result
    }
}
