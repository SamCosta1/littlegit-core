package org.littlegit.core.modifier
import org.littlegit.core.LittleGitCommandCallback
import org.littlegit.core.shell.*


class RepoModifier(private val commandRunner: GitCommandRunner) {

    fun initializeRepo(callback: LittleGitCommandCallback<Unit>?) {
        commandRunner.runCommand(command = GitCommand.InitializeRepo()) { callback?.invoke(null, it) }
    }

    fun commit(message: String, callback: GitCommandRunnerCallback? = null) {
        commandRunner.runCommand(command = GitCommand.Commit(message), callback = callback)
    }
}
