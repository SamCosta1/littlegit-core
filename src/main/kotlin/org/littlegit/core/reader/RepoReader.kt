package org.littlegit.core.reader
import org.littlegit.core.LittleGitCommandResult
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

    fun getGraph(): LittleGitCommandResult<GitGraph> {
        val resultProcessor = { result: GitResult.Success ->
            val commits = LogParser.parse(result.lines)
            GitGraph(commits)
        }

        return commandRunner.runCommand(command = GitCommand.Log(), resultProcessor = resultProcessor)
    }

    fun getRemotes(): LittleGitCommandResult<List<Remote>> {
        val resultProcessor = { result: GitResult.Success -> RemoteParser.parse(result.lines) }
        return commandRunner.runCommand(command = GitCommand.ListRemotes(), resultProcessor = resultProcessor)
    }

    fun getFullCommitList(): LittleGitCommandResult<List<RawCommit>> {
        val resultProcessor = { result: GitResult.Success -> LogParser.parse(result.lines) }

        return commandRunner.runCommand(command = GitCommand.Log(), resultProcessor = resultProcessor)
    }

    fun isInitialized(): LittleGitCommandResult<Boolean> {
        val resultProcessor = { result: GitResult.Success -> result.lines[0] == "true" }

        val res = commandRunner.runCommand(command = GitCommand.IsInitialized(), resultProcessor = resultProcessor)

        return if (res.result is GitResult.Error && res.result.err is GitError.NotARepo) {
            LittleGitCommandResult(false, res.result)
        } else {
            LittleGitCommandResult(res.data, res.result)
        }

    }

    fun getAsciiGraph(): LittleGitCommandResult<String> {
        val resultProcessor = { result: GitResult.Success ->
            val commits = LogParser.parse(result.lines)
            AsciiGraph.getAsciiGraph(commits)
        }

        return commandRunner.runCommand(command = GitCommand.Log(), resultProcessor = resultProcessor)
    }

    fun getFile(ref: String = "", file: String): LittleGitCommandResult<LittleGitFile> {
        val resultProcessor = { result: GitResult.Success -> LittleGitFile(result.lines, file) }
        return commandRunner.runCommand(command = GitCommand.ShowFile(ref, file), resultProcessor = resultProcessor)
    }

    fun getFullCommit(commit: RawCommit) = getFullCommit(commit.hash)

    fun getFullCommit(commit: CommitHash): LittleGitCommandResult<FullCommit> {
        val resultProcessor = { result: GitResult.Success ->
            FullCommitParser.parse(result.lines)
        }

        return commandRunner.runCommand(command = GitCommand.FullCommit(commit), resultProcessor = resultProcessor)
    }
}
