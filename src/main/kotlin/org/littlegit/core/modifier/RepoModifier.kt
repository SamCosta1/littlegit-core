package org.littlegit.core.modifier
import org.littlegit.core.shell.*

class RepoModifier(private val commandRunner: GitCommandRunner) {

    fun initializeRepo(callback: GitCommandRunnerCallback?) {
        commandRunner.runCommand(command = InitializeRepo(), callback = callback)
    }

    fun commit(message: String, callback: GitCommandRunnerCallback? = null) {
        commandRunner.runCommand(command = Commit(message), callback = callback)
    }
}
