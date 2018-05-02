package org.littlegit.core.integration

import org.junit.Test

class ReadingTests: IntegrationBaseTest() {

    @Test fun test() {

        littleGit.repoReader.getGraph { gitGraph, gitResult ->
            println("Ending Commits: " + gitGraph?.getEndingCommits())
            println("Initial Commits: " + gitGraph?.getInitialCommits())
        }
    }
}