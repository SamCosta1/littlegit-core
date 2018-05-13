package org.littlegit.core.parser

import org.littlegit.core.model.GitError
import org.littlegit.core.commandrunner.GitResult
import org.littlegit.core.shell.ShellResult

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

        if (lines.first().startsWith("fatal: The current branch") && lines.first().endsWith("has no upstream branch.")) {
            return GitResult.Error(GitError.NoUpstreamBranch(lines))
        }

        if (lines.first().startsWith("fatal: unable to access") && lines.first().contains("Could not resolve host")) {
            return GitResult.Error(GitError.CannotReadRemote(lines))
        }

        if (lines.size > 1 && lines[1].startsWith("fatal: Could not read from remote repository.")) {
            return GitResult.Error(GitError.CannotReadRemote(lines))
        }
        
        if (lines.first().startsWith("fatal: Path ") && lines.first().endsWith("exists on disk, but not in the index.")) {
            return GitResult.Error(GitError.FileNotInIndex(lines, true))
        }

        if (lines.first().startsWith("fatal: Path ") && lines.first().endsWith("does not exist (neither on disk nor in the index).")) {
            return GitResult.Error(GitError.FileNotInIndex(lines, false))
        }

        if (lines.first().startsWith("fatal:") && lines.first().endsWith("is not a valid remote name")) {
            return GitResult.Error(GitError.InvalidRemoteName(lines))
        }

        if (lines.first().startsWith("fatal: remote") && lines.first().endsWith("already exists.")) {
            return GitResult.Error(GitError.RemoteAlreadyExists(lines))
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
