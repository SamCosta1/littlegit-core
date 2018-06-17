package org.littlegit.core.reader
import org.littlegit.core.LittleGitCommandCallback
import org.littlegit.core.commandrunner.*
import org.littlegit.core.model.FullCommit
import org.littlegit.core.model.GitError
import org.littlegit.core.model.LittleGitFile
import org.littlegit.core.model.RawCommit
import org.littlegit.core.parser.FullCommitParser
import org.littlegit.core.parser.LogParser
import org.littlegit.core.parser.Remote
import org.littlegit.core.parser.RemoteParser

class RepoReader(private val commandRunner: GitCommandRunner) {

    fun getGraph(callback: LittleGitCommandCallback<GitGraph>) {
        val resultProcessor = { result: GitResult.Success ->
            val commits = LogParser.parse(result.lines)
            GitGraph(commits)
        }

        commandRunner.runCommand(command = GitCommand.Log(), resultProcessor = resultProcessor, callback = callback)
    }

    fun getRemotes(callback: LittleGitCommandCallback<List<Remote>>) {
        val resultProcessor = { result: GitResult.Success -> RemoteParser.parse(result.lines) }
        commandRunner.runCommand(command = GitCommand.ListRemotes(), resultProcessor = resultProcessor, callback = callback)
    }

    fun getFullCommitList(callback: LittleGitCommandCallback<List<RawCommit>>) {
        val resultProcessor = { result: GitResult.Success -> LogParser.parse(result.lines) }

        commandRunner.runCommand(command = GitCommand.Log(), resultProcessor = resultProcessor, callback = callback)
    }

    fun isInitialized(callback: LittleGitCommandCallback<Boolean>) {
        val resultProcessor = { result: GitResult.Success -> result.lines[0] == "true" }

        commandRunner.runCommand(command = GitCommand.IsInitialized(), resultProcessor = resultProcessor) { initialized, gitResult ->
            if (gitResult is GitResult.Error && gitResult.err is GitError.NotARepo) {
                callback(false, gitResult)
            } else {
                callback(initialized, gitResult)
            }
        }
    }

    fun getAsciiGraph(callback: LittleGitCommandCallback<String>) {
        val resultProcessor = { result: GitResult.Success ->
            val commits = LogParser.parse(result.lines)
            AsciiGraph.getAsciiGraph(commits)
        }

        commandRunner.runCommand(command = GitCommand.Log(), resultProcessor = resultProcessor, callback = callback)
    }

    fun printAsciiGraph() {
       getAsciiGraph { graph, result ->
           if (graph == null) {
               println(result)
           } else {
               println(graph)
           }
       }
    }

    fun getFile(ref: String = "", file: String, callback: LittleGitCommandCallback<LittleGitFile>) {
        val resultProcessor = { result: GitResult.Success -> LittleGitFile(result.lines, file) }
        commandRunner.runCommand(command = GitCommand.ShowFile(ref, file), resultProcessor = resultProcessor, callback = callback);
    }

    fun getFullCommit(commit: RawCommit, callback: LittleGitCommandCallback<FullCommit>) = getFullCommit(commit.hash, callback)

    fun getFullCommit(commit: CommitHash, callback: LittleGitCommandCallback<FullCommit>) {
        commandRunner.runCommand(command = GitCommand.FullCommit(commit)) { result ->

            when (result) {
                is GitResult.Success -> callback(FullCommitParser.parse(result.lines), result)
                is GitResult.Error -> callback(null, result)
            }

        }
    }
}
