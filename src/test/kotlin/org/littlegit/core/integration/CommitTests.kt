package org.littlegit.core.integration

import org.junit.Assert.*
import org.junit.Test
import org.littlegit.core.model.GitError
import org.littlegit.core.commandrunner.GitResult
import org.littlegit.core.helper.TestCommandHelper

class CommitTests: BaseIntegrationTest() {


    @Test fun testValidCommit() {
        val commitMessage = "test message"

        testFolder.newFile("testFile")
        val commandHelper = TestCommandHelper(testFolder.root).init().addAll()

        littleGit.repoModifier.commit(commitMessage) { _, result ->

            assertTrue("Result was a success", result is GitResult.Success)
            assertTrue("RawCommit message is as expected", commandHelper.getLastCommitMessage() == commitMessage)
        }
    }

    @Test fun testCommitBeforeInit() {
        littleGit.repoModifier.commit("msg") { _, result ->
            assertTrue("RawCommit rejected", result is GitResult.Error && result.err is GitError.NotARepo)
        }
    }

    @Test fun testCommitAfterInitBeforeAdd() {
        TestCommandHelper(testFolder.root).init()

        littleGit.repoModifier.commit("msg") { _, result ->
            assertTrue("RawCommit rejected", result is GitResult.Error && result.err is GitError.NothingToCommit)
        }
    }
}