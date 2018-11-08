package org.littlegit.core.modifier

import org.littlegit.core.LittleGitCommandResult
import org.littlegit.core.commandrunner.GitCommand
import org.littlegit.core.commandrunner.GitCommandRunner
import org.littlegit.core.commandrunner.GitResult
import java.nio.file.Path
import java.nio.file.Paths

class ConfigModifier(private val commandRunner: GitCommandRunner) {

    fun setSshKeyPath(path: Path): LittleGitCommandResult<Void> {
        return commandRunner.runCommand(command = GitCommand.SetSshKeyPath(path))
    }

    fun getSshKeyPath(): LittleGitCommandResult<Path?> {
        val resultProcessor = { result: GitResult.Success ->
            if (result.lines.isEmpty()) {
                null
            } else {
                Paths.get(result.lines.first().removePrefix("ssh -i "))
            }
        }

        return commandRunner.runCommand(command = GitCommand.GetSshKeyPath(), resultProcessor = resultProcessor)
    }

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