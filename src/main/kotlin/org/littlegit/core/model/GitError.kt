package org.littlegit.core.model

sealed class GitError(val error: List<String>) {

    val errString: String; get() = error.joinToString("\n")

    data class LocalChangesWouldBeOverwritten(private val err: List<String>): GitError(err)
    data class Unknown(private val err: List<String>): GitError(err)
    data class NotARepo(private val err: List<String>): GitError(err)
    data class NothingToCommit(private val err: List<String>): GitError(err)
    data class NoRemote(private val err: List<String>): GitError(err)
    data class NoUpstreamBranch(private val err: List<String>): GitError(err)
    data class CannotReadRemote(private val err: List<String>): GitError(err)
    data class InvalidRemoteInfo(private val err: List<String>): GitError(err)
    data class RemoteAlreadyExists(private val err: List<String>): GitError(err)
    data class FileNotInIndex(private val err: List<String>, val fileExistsOnDisk: Boolean): GitError(err)
}
