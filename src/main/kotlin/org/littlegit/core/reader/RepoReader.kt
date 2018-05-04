package org.littlegit.core.reader
import org.littlegit.core.LittleGitCommandCallback
import org.littlegit.core.model.RawCommit
import org.littlegit.core.shell.*

class RepoReader(private val commandRunner: GitCommandRunner) {

    fun getGraph(callback: LittleGitCommandCallback<GitGraph>) {
        commandRunner.runCommand(command = GitCommand.Log()) { result ->

            if (result is GitResult.Success) {
                val commits = GitCommand.Log.parse(result.lines)
                callback(GitGraph(commits), result)
            } else {
                callback(null, result)
            }
        }
    }

    fun getFullCommitList(callback: LittleGitCommandCallback<List<RawCommit>>) {
        commandRunner.runCommand(command = GitCommand.Log()) { result ->
            if (result is GitResult.Success) {
                val commits = GitCommand.Log.parse(result.lines)
                callback((commits, result)
            } else {
                callback(null, result)
            }
        }
    }

    fun isInitialized(callback: LittleGitCommandCallback<Boolean>) {

        commandRunner.runCommand(command = GitCommand.IsInitialized()) { result ->

            when (result) {
                is GitResult.Success -> callback(result.lines[0] == "true", result)
                is GitResult.Error -> callback(if (result.err is GitError.NotARepo) false else null, result)
            }
        }
    }
}
