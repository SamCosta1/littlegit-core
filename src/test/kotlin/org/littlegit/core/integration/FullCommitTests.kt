package org.littlegit.core.integration

import junit.framework.TestCase.*
import org.junit.Test
import org.littlegit.core.commandrunner.GitResult
import org.littlegit.core.helper.AssertHelper
import org.littlegit.core.helper.TestCommandHelper
import org.littlegit.core.model.FullCommit
import java.time.Instant
import java.time.OffsetDateTime
import java.time.ZoneId

class FullCommitTests: BaseIntegrationTest() {

    @Test fun testValidCommit() {
        val commitMessage = "Message"
        val commandHelper = TestCommandHelper(testFolder.root)
                        .init()
                        .initConfig()
                        .writeToFile("file.txt", "Text")
                        .addAll()
                        .commit(commitMessage)

        val hash = commandHelper.getLastCommitHash()
        val timestamp = commandHelper.getLastCommitTimeStamp()

        val expectedCommit = FullCommit(
                hash,
                listOf("refs/heads/master"),
                emptyList(),
                OffsetDateTime.ofInstant(Instant.ofEpochMilli(timestamp.toLong() * 1000), ZoneId.systemDefault()),
                TestCommandHelper.DEFAULT_EMAIL,
                commitMessage,
                true,
                commitBody = listOf(commitMessage)
        )

        littleGit.repoReader.getFullCommit(hash) { fullCommit, result ->
            assertTrue("Result was success", result is GitResult.Success)

            assertNotNull(fullCommit)
            AssertHelper.assertFullCommit(expectedCommit, fullCommit!!, ignoreDiff = true)
        }
    }
}