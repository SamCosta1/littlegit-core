package org.littlegit.core.commandrunner

import org.littlegit.core.model.GitError
import org.littlegit.core.parser.GitResultParser
import org.littlegit.core.shell.ShellRunner

typealias GitCommandRunnerCallback = (GitResult) -> Unit

sealed class GitResult {
    data class Success(val lines: List<String>): GitResult()
    data class Error(val err: GitError): GitResult()
}

class GitCommandRunner {

    private var repoPath: String? = null
    private val shellRunner = ShellRunner()

    fun initializeRepoDirectory(path: String): GitCommandRunner {
        if (path.isNotBlank()) {
            repoPath = path
        }

        return this
    }

    fun runCommand(repoDir: String? = null, command: GitCommand, callback: GitCommandRunnerCallback?) {
        if (repoPath == null && repoDir == null) {
            throw RuntimeException("Repo path must be supplied or GitCommandRunner initialised")
        }

        val basePath = repoDir ?: repoPath

        basePath?.let {
            shellRunner.runCommand(it, command.command) {
                callback?.invoke(GitResultParser.parseShellResult(it))
            }
        }
    }
}