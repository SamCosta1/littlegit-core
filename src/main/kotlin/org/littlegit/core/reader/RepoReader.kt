package org.littlegit.core.reader
import org.littlegit.core.shell.GitCommandRunner
import org.littlegit.core.shell.GitResult
import org.littlegit.core.shell.IsInitialized

class RepoReader(private val commandRunner: GitCommandRunner) {

    private val graphReader = GraphReader()

    fun readRepoGraph() = graphReader.readRepoGraph()

    fun isInitialized(callback: (Boolean) -> Unit) {

        commandRunner.runCommand(command = IsInitialized()) { result ->
            when (result) {
                is GitResult.Success -> callback(result.lines[0] == "true")
                is GitResult.Error -> callback(false)
            }
        }
    }
}
