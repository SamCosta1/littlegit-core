package org.littlegit.core.integration

import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import org.junit.Test
import org.littlegit.core.commandrunner.GitCommand
import org.littlegit.core.commandrunner.GitResult
import org.littlegit.core.helper.TestCommandHelper
import org.littlegit.core.util.joinWithSpace

class FullCommitTests: BaseIntegrationTest() {

    @Test fun testValidCommit() {
        val commitMessage = "Message"
        val hash = TestCommandHelper(testFolder.root)
                        .init()
                        .initConfig()
                        .writeToFile("file.txt", "Text")
                        .addAll()
                        .commit(commitMessage)
                        .getLastCommitHash()

        println(GitCommand.FullCommit(hash).command.joinWithSpace())
        littleGit.repoReader.getFullCommit(hash) { fullCommit, result ->
            assertTrue("Result was success", result is GitResult.Success)

            fullCommit?.let { commit ->
                assertEquals("Commit message correct", commitMessage, commit.commitBody)
            }
        }
    }
}