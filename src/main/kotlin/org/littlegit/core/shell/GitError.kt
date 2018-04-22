package org.littlegit.core.shell

enum class GitError {
    LocalChangesWouldBeOverwritten,
    Unknown,
    NotARepo;

    companion object {
        fun parseError(lines: List<String>): GitError {
            if (lines.isEmpty()) {
                return Unknown
            }

            if (lines[0].startsWith("fatal: Not a git repository", ignoreCase = true)) {
                return NotARepo
            }

            if (lines[0].startsWith("error: Your local changes to the following files would be overwritten by checkout", ignoreCase = true)) {
                return LocalChangesWouldBeOverwritten
            }

            return Unknown
        }
    }
}
