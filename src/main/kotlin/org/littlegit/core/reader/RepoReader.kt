package org.littlegit.core.reader
import org.littlegit.core.shell.ShellRunner

class RepoReader {

    private val graphReader = GraphReader()

    fun readRepoGraph() = graphReader.readRepoGraph()
}
