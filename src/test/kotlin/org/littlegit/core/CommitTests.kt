package org.littlegit.core

import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import org.littlegit.core.shell.GitError
import org.littlegit.core.shell.GitResult

class CommitTests {

    @Rule
    @JvmField var testFolder = TemporaryFolder()

    lateinit var littleGit: LittleGitCore

    @Before
    fun setup() {
        littleGit = LittleGitCore(testFolder.root.path)
    }

    @Test fun testValidCommit() {
        val commitMessage = "test message"

        testFolder.newFile("testFile")
        val commandHelper = TestCommandHelper(testFolder.root).init().addAll()

        littleGit.repoModifier.commit(commitMessage) {

            assertTrue("Result was a success", it is GitResult.Success)
            assertTrue("Commit message is as expected", commandHelper.getLastCommitMessage() == commitMessage)
        }
    }

    @Test fun testCommitBeforeInit() {
        littleGit.repoModifier.commit("msg") {
            assertTrue("Commit rejected", it is GitResult.Error && it.err is GitError.NotARepo)
        }
    }

    @Test fun testCommitAfterInitBeforeAdd() {
        TestCommandHelper(testFolder.root).init()

        littleGit.repoModifier.commit("msg") {
            print(it)
            assertTrue("Commit rejected", it is GitResult.Error && it.err is GitError.NothingToCommit)
        }
    }
}