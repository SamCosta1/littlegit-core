package org.littlegit.core.modifier

import org.littlegit.core.LittleGitCommandCallback
import org.littlegit.core.commandrunner.GitCommand
import org.littlegit.core.commandrunner.GitCommandRunner
import org.littlegit.core.commandrunner.GitResult

class ConfigModifier(private val commandRunner: GitCommandRunner) {

    fun setName(name: String, global: Boolean = false, callback: LittleGitCommandCallback<String>) {
        val resultProcessor = { _: GitResult.Success -> name }
        commandRunner.runCommand(command = GitCommand.SetUserName(name, global), resultProcessor = resultProcessor, callback = callback)
    }

    fun setEmail(email: String, global: Boolean = false, callback: LittleGitCommandCallback<String>) {
        val resultProcessor = { _: GitResult.Success -> email }
        commandRunner.runCommand(command = GitCommand.SetUserEmail(email, global), resultProcessor = resultProcessor, callback = callback)
    }

    fun getEmail(global: Boolean = false, callback: LittleGitCommandCallback<String>) {
        val resultProcessor = { result: GitResult.Success -> result.lines[0] }
        commandRunner.runCommand(command = GitCommand.GetUserEmail(global), resultProcessor = resultProcessor, callback = callback)
    }

    fun getName(global: Boolean = false, callback: LittleGitCommandCallback<String>) {
        val resultProcessor = { result: GitResult.Success -> result.lines[0] }
        commandRunner.runCommand(command = GitCommand.GetUserName(global), resultProcessor = resultProcessor, callback = callback)
    }
}