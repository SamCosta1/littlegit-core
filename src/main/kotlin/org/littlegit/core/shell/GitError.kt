package org.littlegit.core.shell

sealed class GitError(val error: String) {
    data class LocalChangesWouldBeOverwritten(private val err: String): GitError(err)
    data class Unknown(private val err: String): GitError(err)
    data class NotARepo(private val err: String): GitError(err)

    companion object {
        fun parseError(lines: List<String>): GitError {
            val errString = lines.joinToString("\n")

            if (lines.isEmpty()) {
                return Unknown(errString)
            }

            if (lines[0].startsWith("fatal: Not a git repository", ignoreCase = true)) {
                return NotARepo(errString)
            }

            if (lines[0].startsWith("error: Your local changes to the following files would be overwritten by checkout", ignoreCase = true)) {
                return LocalChangesWouldBeOverwritten(errString)
            }

            return Unknown(errString)
        }
    }
}
