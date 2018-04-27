package org.littlegit.core.shell

sealed class GitError(val error: List<String>) {

    val errString: String
        get() = buildErrorString(error)

    data class LocalChangesWouldBeOverwritten(private val err: List<String>): GitError(err)
    data class Unknown(private val err: List<String>): GitError(err)
    data class NotARepo(private val err: List<String>): GitError(err)
    data class NothingToCommit(private val err: List<String>): GitError(err)

    private fun buildErrorString(lines: List<String>) = lines.joinToString("\n")
}
