package org.littlegit.core.modifier
import org.littlegit.core.LittleGitCommandCallback
import org.littlegit.core.commandrunner.GitCommand
import org.littlegit.core.commandrunner.GitCommandRunner
import org.littlegit.core.commandrunner.GitCommandRunnerCallback


class RepoModifier(private val commandRunner: GitCommandRunner) {

    fun initializeRepo(callback: LittleGitCommandCallback<Unit>?) {
        commandRunner.runCommand(command = GitCommand.InitializeRepo()) { callback?.invoke(null, it) }
    }

    fun commit(message: String, callback: GitCommandRunnerCallback? = null) {
        commandRunner.runCommand(command = GitCommand.Commit(message), callback = callback)
    }

    fun push(remote: String? = null, branch: String? = null, callback: LittleGitCommandCallback<Unit>? = null) {
        commandRunner.runCommand(command = GitCommand.Push(remote, branch)) {
            callback?.invoke(null, it)
        }
    }

}
