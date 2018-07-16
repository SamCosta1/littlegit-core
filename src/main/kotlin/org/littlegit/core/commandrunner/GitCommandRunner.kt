package org.littlegit.core.commandrunner

import org.littlegit.core.LittleGitCommandResult
import org.littlegit.core.model.GitError
import org.littlegit.core.parser.GitResultParser
import org.littlegit.core.shell.ShellRunner
import org.littlegit.core.shell.ShellRunnerLocal

typealias ResultProcessor<T> = (GitResult.Success) -> T

sealed class GitResult {
    data class Success(val lines: List<String>): GitResult()
    data class Error(val err: GitError): GitResult()
}

class GitCommandRunner(private val shellRunner: ShellRunner) {

    fun <T>runCommand(command: GitCommand, resultProcessor: ResultProcessor<T>? = null): LittleGitCommandResult<T> {

        val shellResult = shellRunner.runCommand(command.command)
        val gitResult = GitResultParser.parseShellResult(shellResult)
        val processedResult: T? = when (gitResult) {
            is GitResult.Success -> resultProcessor?.invoke(gitResult)
            else -> null
        }

        return LittleGitCommandResult(processedResult, gitResult)
    }
}