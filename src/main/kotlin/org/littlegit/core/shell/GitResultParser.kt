package org.littlegit.core.shell

object GitResultParser {

    fun parseShellResult(shellResult: ShellResult): GitResult {
        return when (shellResult) {
            is ShellResult.Error -> parseErrorResult(shellResult)
            is ShellResult.Success -> parseSuccessResult(shellResult)
        }
    }

    private fun parseErrorResult(errResult: ShellResult.Error): GitResult.Error {
        val lines = errResult.lines

        if (lines.first().startsWith("fatal: Not a git repository", ignoreCase = true)) {
            return GitResult.Error(GitError.NotARepo(lines))
        }

        if (lines.first().startsWith("error: Your local changes to the following files would be overwritten by checkout", ignoreCase = true)) {
            return GitResult.Error(GitError.LocalChangesWouldBeOverwritten(lines))
        }

        return GitResult.Error(GitError.Unknown(lines))
    }

    private fun parseSuccessResult(successResult: ShellResult.Success): GitResult {
        val lines = successResult.lines
        val lastLine = lines.last()
        if (lastLine.startsWith("nothing added to commit") || lastLine.startsWith("nothing to commit")) {
            return GitResult.Error(GitError.NothingToCommit(lines))
        }

        return GitResult.Success(lines)
    }
}
