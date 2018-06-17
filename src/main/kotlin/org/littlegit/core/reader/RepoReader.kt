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
        commandRunner.runCommand(command = GitCommand.Log()) { result ->

            if (result is GitResult.Success) {
                val commits = LogParser.parse(result.lines)
                callback(GitGraph(commits), result)
            } else {
                callback(null, result)
            }
        }
    }

    fun getRemotes(callback: LittleGitCommandCallback<List<Remote>>) {
        commandRunner.runCommand(command = GitCommand.ListRemotes()) { result ->

            if (result is GitResult.Success) {
                val remotes = RemoteParser.parse(result.lines)
                callback(remotes, result)
            } else {
                callback(null, result)
            }
        }
    }

    fun getFullCommitList(callback: LittleGitCommandCallback<List<RawCommit>>) {
        commandRunner.runCommand(command = GitCommand.Log()) { result ->
            if (result is GitResult.Success) {
                val commits = LogParser.parse(result.lines)
                callback(commits, result)
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

    fun getAsciiGraph(callback: LittleGitCommandCallback<String>) {
        getFullCommitList { commits, result ->
            if (result is GitResult.Error || commits == null) {
                callback(null, result)
            } else {
                callback(AsciiGraph.getAsciiGraph(commits), result)
            }
        }
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
        commandRunner.runCommand(command = GitCommand.ShowFile(ref, file), resultProcessor = resultProcessor, callback = callback)
    }

    fun getFullCommit(commit: RawCommit, callback: LittleGitCommandCallback<FullCommit>) = getFullCommit(commit.hash, callback)

    fun getFullCommit(commit: CommitHash, callback: LittleGitCommandCallback<FullCommit>) {
        val resultProcessor = { result: GitResult.Success ->
            FullCommitParser.parse(result.lines)
        }

        commandRunner.runCommand(command = GitCommand.FullCommit(commit), resultProcessor = resultProcessor, callback = callback)
    }
}
