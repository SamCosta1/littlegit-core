package org.littlegit.core.integration

import org.junit.Assert.*
import org.junit.Test
import org.littlegit.core.model.GitError
import org.littlegit.core.commandrunner.GitResult

class CommitTests: BaseIntegrationTest() {


    @Test fun testValidCommit() {
        val commitMessage = "test message"

        testFolder.newFile("testFile")
        val commandHelper = TestCommandHelper(testFolder.root).init().addAll()

        littleGit.repoModifier.commit(commitMessage) {

            assertTrue("Result was a success", it is GitResult.Success)
            assertTrue("RawCommit message is as expected", commandHelper.getLastCommitMessage() == commitMessage)
        }
    }

    @Test fun testCommitBeforeInit() {
        littleGit.repoModifier.commit("msg") {
            assertTrue("RawCommit rejected", it is GitResult.Error && it.err is GitError.NotARepo)
        }
    }

    @Test fun testCommitAfterInitBeforeAdd() {
        TestCommandHelper(testFolder.root).init()

        littleGit.repoModifier.commit("msg") {
            print(it)
            assertTrue("RawCommit rejected", it is GitResult.Error && it.err is GitError.NothingToCommit)
        }
    }
}