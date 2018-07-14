package org.littlegit.core.commandrunner

import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.runBlocking
import org.littlegit.core.LittleGitCommandResult
import org.littlegit.core.model.GitError
import org.littlegit.core.parser.GitResultParser
import org.littlegit.core.shell.ShellRunner

typealias ResultProcessor<T> = (GitResult.Success) -> T

sealed class GitResult {
    data class Success(val lines: List<String>): GitResult()
    data class Error(val err: GitError): GitResult()
}

class GitCommandRunner(private val shellRunner: ShellRunner) {

    private var repoPath: String? = null

    fun initializeRepoDirectory(path: String): GitCommandRunner {
        if (path.isNotBlank()) {
            repoPath = path
        }

        return this
    }

    fun <T>runCommand(repoDir: String? = null, command: GitCommand, resultProcessor: ResultProcessor<T>? = null): LittleGitCommandResult<T> {
        val repoPath = repoPath
        if (repoPath == null && repoDir == null) {
            throw RuntimeException("Repo path must be supplied or GitCommandRunner initialised")
        }

        val basePath = repoDir ?: repoPath

        val shellResult = shellRunner.runCommand(basePath!!, command.command)
        val gitResult = GitResultParser.parseShellResult(shellResult)
        val processedResult: T? = when (gitResult) {
            is GitResult.Success -> resultProcessor?.invoke(gitResult)
            else -> null
        }

        return LittleGitCommandResult(processedResult, gitResult)
    }


}