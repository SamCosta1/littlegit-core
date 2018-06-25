package org.littlegit.core.integration

import org.junit.Assert.*
import org.junit.Ignore
import org.junit.Test
import org.littlegit.core.model.GitError
import org.littlegit.core.commandrunner.GitResult
import org.littlegit.core.helper.TestCommandHelper
import org.littlegit.core.model.FileDiff
import org.littlegit.core.model.Hunk

class CommitTests: BaseIntegrationTest() {


    @Test fun testValidCommit() {
        val commitMessage = "test message"

        testFolder.newFile("testFile")
        val commandHelper = TestCommandHelper(testFolder.root)
                .init()
                .initConfig()
                .addAll()

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
        TestCommandHelper(testFolder.root).init().initConfig()

        littleGit.repoModifier.commit("msg") { _, result ->
            assertTrue("RawCommit rejected", result is GitResult.Error && result.err is GitError.NothingToCommit)
        }
    }

    @Ignore
    @Test fun testMultiLineCommitMessage() {
        val fileName = "testFile.txt"
        val commitMessage = """"This is a subject line
        This is the main body
        """.trimIndent()

        testFolder.newFile(fileName)
        val commandHelper = TestCommandHelper(testFolder.root)
        commandHelper.init().initConfig().addAll()

        littleGit.repoModifier.commit(commitMessage) { _, result ->

            littleGit.repoReader.getFullCommit(commandHelper.getLastCommitHash()) { fullCommit, res ->
                assertEquals(commitMessage, fullCommit?.commitBody)

                val fileDiffs = fullCommit?.diff?.fileDiffs
                assertEquals(1, fileDiffs?.size)
                assertTrue(fileDiffs?.get(0) is FileDiff.NewFile)

                val newFile = fileDiffs?.get(0) as FileDiff.NewFile
                assertEquals(fileName, newFile.filePath)
                assertEquals(emptyList<Hunk>(), newFile.hunks)
            }
        }
    }
}