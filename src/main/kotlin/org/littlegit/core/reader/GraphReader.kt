package org.littlegit.core.reader

import org.littlegit.core.shell.ShellRunner

class GraphReader {

    fun readRepoGraph(): GitGraph {
     //   ShellRunner.runCommand("git" )
        return GitGraph()
    }
}
