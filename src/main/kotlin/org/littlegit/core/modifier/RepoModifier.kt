package org.littlegit.core.modifier
import org.littlegit.core.LittleGitCommandResult
import org.littlegit.core.commandrunner.GitCommand
import org.littlegit.core.commandrunner.GitCommandRunner
import org.littlegit.core.commandrunner.GitResult
import org.littlegit.core.model.GitError
import org.littlegit.core.util.ListUtils
import java.io.File


class RepoModifier(private val commandRunner: GitCommandRunner) {

    fun initializeRepo(bare: Boolean = false, name: String? = null): LittleGitCommandResult<Unit>
            = commandRunner.runCommand(command = GitCommand.InitializeRepo(bare, name))

    fun commit(message: String): LittleGitCommandResult<Unit> = commit(listOf(message))


    fun commit(message: List<String>): LittleGitCommandResult<Unit> {

        var result: LittleGitCommandResult<Unit>? = null
        var tempFile: File? = null
        try {
            tempFile = File.createTempFile("littlegit-commit-message", System.nanoTime().toString())
            ListUtils.writeToFile(message, tempFile)

            result  = commandRunner.runCommand(command = GitCommand.Commit(tempFile))
            tempFile.delete()

        } catch(e: Exception)  {
            tempFile?.delete()
        }

        return result ?: LittleGitCommandResult(null, GitResult.Error(GitError.Unknown(listOf())))
    }

    fun push(remote: String? = null, branch: String? = null, setUpstream: Boolean = false): LittleGitCommandResult<Unit> {
        if (setUpstream && ( branch.isNullOrBlank() || remote.isNullOrBlank() )) {
            throw IllegalArgumentException("Remote and branch must be non empty when setting upstream")
        }

        val command = if (setUpstream) {
            GitCommand.PushSetUpstream(remote!!, branch!!)
        } else {
            GitCommand.Push(remote, branch)
        }

        return commandRunner.runCommand(command = command)
    }

    fun addRemote(remoteName: String, remoteUrl: String): LittleGitCommandResult<Unit>
            = commandRunner.runCommand(command = GitCommand.AddRemote(remoteName, remoteUrl))

}
