package org.littlegit.core.integration

import org.junit.Test

class LogTests: BaseIntegrationTest() {

    @Test fun testLog() {
        TestCommandHelper(testFolder.root).init()
        littleGit.repoReader.getGraph { graph, result ->
            println(graph)
            println(result)
        }
    }
}