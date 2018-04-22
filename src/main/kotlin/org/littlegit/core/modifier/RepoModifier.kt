package org.littlegit.core.modifier
import org.littlegit.core.shell.GitCommandRunner
import org.littlegit.core.shell.GitCommandRunnerCallback
import org.littlegit.core.shell.InitializeRepo

class RepoModifier(private val commandRunner: GitCommandRunner) {

    fun initializeRepo(callback: GitCommandRunnerCallback) {
        commandRunner.runCommand(command = InitializeRepo(), callback = callback)
    }


}
