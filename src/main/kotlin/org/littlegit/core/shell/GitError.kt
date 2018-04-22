package org.littlegit.core.shell

enum class GitError {
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

            return Unknown
        }
    }


}
