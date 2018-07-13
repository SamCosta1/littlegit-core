package org.littlegit.core.modifier

import org.littlegit.core.LittleGitCommandResult
import org.littlegit.core.commandrunner.GitCommand
import org.littlegit.core.commandrunner.GitCommandRunner
import org.littlegit.core.commandrunner.GitResult

class ConfigModifier(private val commandRunner: GitCommandRunner) {

    fun setName(name: String, global: Boolean = false): LittleGitCommandResult<String> {
        val resultProcessor = { _: GitResult.Success -> name }
        return commandRunner.runCommand(command = GitCommand.SetUserName(name, global), resultProcessor = resultProcessor)
    }

    fun setEmail(email: String, global: Boolean = false): LittleGitCommandResult<String> {
        val resultProcessor = { _: GitResult.Success -> email }
        return commandRunner.runCommand(command = GitCommand.SetUserEmail(email, global), resultProcessor = resultProcessor)
    }

    fun getEmail(global: Boolean = false): LittleGitCommandResult<String> {
        val resultProcessor = { result: GitResult.Success -> result.lines[0] }
        return commandRunner.runCommand(command = GitCommand.GetUserEmail(global), resultProcessor = resultProcessor)
    }

    fun getName(global: Boolean = false): LittleGitCommandResult<String> {
        val resultProcessor = { result: GitResult.Success -> result.lines[0] }
        return commandRunner.runCommand(command = GitCommand.GetUserName(global), resultProcessor = resultProcessor)
    }
}