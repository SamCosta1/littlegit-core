package org.littlegit.core.reader
import org.littlegit.core.LittleGitCommandCallback
import org.littlegit.core.shell.*

class RepoReader(private val commandRunner: GitCommandRunner) {

    fun getGraph(callback: LittleGitCommandCallback<GitGraph>) {
        commandRunner.runCommand(command = Log()) { result ->
            callback.invoke(null, result)
        }
    }

    fun isInitialized(callback: LittleGitCommandCallback<Boolean>) {

        commandRunner.runCommand(command = IsInitialized()) { result ->

            when (result) {
                is GitResult.Success -> callback(result.lines[0] == "true", result)
                is GitResult.Error -> callback(if (result.err is GitError.NotARepo) false else null, result)
            }
        }
    }
}
