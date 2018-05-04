package org.littlegit.core.modifier

import org.littlegit.core.LittleGitCommandCallback
import org.littlegit.core.shell.GitCommand
import org.littlegit.core.shell.GitCommandRunner
import org.littlegit.core.shell.GitResult

class ConfigModifier(private val commandRunner: GitCommandRunner) {

    fun setName(name: String, global: Boolean = false, callback: LittleGitCommandCallback<String>) {
        commandRunner.runCommand(command = GitCommand.SetUserName(name, global)) {
            if (it is GitResult.Success) {
                callback(name, it)
            } else {
                callback(null, it)
            }
        }
    }

    fun setEmail(email: String, global: Boolean = false, callback: LittleGitCommandCallback<String>) {
        commandRunner.runCommand(command = GitCommand.SetUserEmail(email, global)) {
            if (it is GitResult.Success) {
                callback(email, it)
            } else {
                callback(null, it)
            }
        }
    }

    fun getEmail(global: Boolean = false, callback: LittleGitCommandCallback<String>) {
        commandRunner.runCommand(command = GitCommand.GetUserEmail(global)) {
            if (it is GitResult.Success) {
                callback(it.lines[0], it)
            } else {
                callback(null, it)
            }
        }
    }

    fun getName(global: Boolean = false, callback: LittleGitCommandCallback<String>) {
        commandRunner.runCommand(command = GitCommand.GetUserName(global)) {
            if (it is GitResult.Success) {
                callback(it.lines[0], it)
            } else {
                callback(null, it)
            }
        }
    }


}