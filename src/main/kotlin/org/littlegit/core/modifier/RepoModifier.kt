package org.littlegit.core.modifier
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.runBlocking
import org.littlegit.core.LittleGitCommandCallback
import org.littlegit.core.commandrunner.GitCommand
import org.littlegit.core.commandrunner.GitCommandRunner
import org.littlegit.core.util.ListUtils
import java.io.File


class RepoModifier(private val commandRunner: GitCommandRunner) {

    fun initializeRepo(callback: LittleGitCommandCallback<Unit>? = null) {
        commandRunner.runCommand(command = GitCommand.InitializeRepo(), callback = callback)
    }

    fun commit(message: String, callback: LittleGitCommandCallback<Unit>? = null) {
        commit(listOf(message), callback)
    }

    fun commit(message: List<String>, callback: LittleGitCommandCallback<Unit>? = null) {

        var tempFile: File? = null
        try {
            tempFile = File.createTempFile("littlegit-commit-message", System.nanoTime().toString())
            async { ListUtils.writeToFile(message, tempFile) }

            runBlocking {
                commandRunner.runCommand<Unit>(command = GitCommand.Commit(tempFile), callback = { _, result ->
                    tempFile.delete()
                    callback?.invoke(null, result)
                })
            }
        } catch(e: Exception)  {
            tempFile?.delete()
        }
    }

    fun push(remote: String? = null, branch: String? = null, setUpstream: Boolean = false, callback: LittleGitCommandCallback<Unit>? = null) {
        if (setUpstream && ( branch.isNullOrBlank() || remote.isNullOrBlank() )) {
            throw IllegalArgumentException("Remote and branch must be non empty when setting upstream")
        }

        val command = if (setUpstream) {
            GitCommand.PushSetUpstream(remote!!, branch!!)
        } else {
            GitCommand.Push(remote, branch)
        }

        commandRunner.runCommand(command = command, callback = callback)
    }

    fun addRemote(remoteName: String, remoteUrl: String, callback: LittleGitCommandCallback<Unit>?) {
        commandRunner.runCommand(command = GitCommand.AddRemote(remoteName, remoteUrl), callback = callback)
    }
}
