package org.littlegit.core.shell

object GitResultParser {

    fun parseShellResult(shellResult: ShellResult): GitResult {
        return when (shellResult) {
            is ShellResult.Error -> parseErrorResult(shellResult)
            is ShellResult.Success -> parseSuccessResult(shellResult)
        }
    }

    private fun parseErrorResult(errResult: ShellResult.Error): GitResult.Error {
        val lines = cleanLines(errResult.lines.toMutableList())

        if (lines.isEmpty()) {
            return GitResult.Error(GitError.Unknown(lines))
        }

        if (lines.first().startsWith("fatal: Not a git repository", ignoreCase = true)) {
            return GitResult.Error(GitError.NotARepo(lines))
        }

        if (lines.first().startsWith("error: Your local changes to the following files would be overwritten by checkout", ignoreCase = true)) {
            return GitResult.Error(GitError.LocalChangesWouldBeOverwritten(lines))
        }

        if (lines.first().startsWith("fatal: No remote repository specified") || lines.first().startsWith("fatal: No configured push destination")) {
            return GitResult.Error(GitError.NoRemote(lines))
        }

        if (lines.first().startsWith("fatal: The current branch") && lines.last().endsWith("has no upstream branch")) {
            return GitResult.Error(GitError.NoUpstreamBranch(lines))
        }

        return GitResult.Error(GitError.Unknown(lines))
    }

    private fun parseSuccessResult(successResult: ShellResult.Success): GitResult {
        val lines = cleanLines(successResult.lines.toMutableList())

        if (lines.isNotEmpty()) {
            val lastLine = lines.last()
            if (lastLine.startsWith("nothing added to commit") || lastLine.startsWith("nothing to commit")) {
                return GitResult.Error(GitError.NothingToCommit(lines))
            }
        }

        return GitResult.Success(lines)
    }

    private fun cleanLines(lines: MutableList<String>): List<String> {
        return lines.dropWhile { it.isBlank() }
    }
}
