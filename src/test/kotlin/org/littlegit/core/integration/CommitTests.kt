package org.littlegit.core.integration

import org.junit.Assert.*
import org.junit.Ignore
import org.junit.Test
import org.littlegit.core.LittleGitCore
import org.littlegit.core.model.GitError
import org.littlegit.core.commandrunner.GitResult
import org.littlegit.core.helper.TestCommandHelper
import org.littlegit.core.model.FileDiff
import org.littlegit.core.model.Hunk
import org.littlegit.core.util.joinWithNewLines

class CommitTests: BaseIntegrationTest() {


    @Test fun testValidCommit() {
        val commitMessage = "test message"

        testFolder.newFile("testFile")
        val commandHelper = TestCommandHelper(testFolder.root)
                .init()
                .initConfig()
                .addAll()

        val result = littleGit.repoModifier.commit(commitMessage).result

        assertTrue("Result was a success", result is GitResult.Success)
        assertTrue("RawCommit message is as expected", commandHelper.getLastCommitMessage() == commitMessage)

    }

    @Test fun testCommitBeforeInit() {
        val result = littleGit.repoModifier.commit("msg").result
        assertTrue("RawCommit rejected", result is GitResult.Error && result.err is GitError.NotARepo)
    }

    @Test fun testCommitAfterInitBeforeAdd() {
        TestCommandHelper(testFolder.root).init().initConfig()

        val result = littleGit.repoModifier.commit("msg").result
        assertTrue("RawCommit rejected", result is GitResult.Error && result.err is GitError.NothingToCommit)
    }

    @Test fun testMultiLineCommitMessage() {
        val fileName = "testFile.txt"
        val commitSubject = "This is a subject line"
        val commitMessage = listOf(
                commitSubject,
                "",
                "First body line",
                "Second body line"
        )

        testFolder.newFile(fileName)
        val commandHelper = TestCommandHelper(testFolder.root)
        commandHelper.init().initConfig().addAll()

        littleGit.repoModifier.commit(commitMessage)

        val result = littleGit.repoReader.getFullCommit(commandHelper.getLastCommitHash())
        val fullCommit = result.data

        assertEquals(commitMessage, fullCommit?.commitBody)

        val fileDiffs = fullCommit?.diff?.fileDiffs
        assertEquals(1, fileDiffs?.size)
        assertTrue(fileDiffs?.get(0) is FileDiff.NewFile)

        val newFile = fileDiffs?.get(0) as FileDiff.NewFile
        assertEquals(fileName, newFile.filePath)
        assertEquals(emptyList<Hunk>(), newFile.hunks)
        assertEquals(commitSubject, fullCommit.commitSubject)

    }

    // Lines with hashes are usually treated as comments and not included
    // in commit messages, littlegit should ensure this doesn't happen and that the
    // Lines beginning in hashes are present
    @Test fun testMultiLineCommitMessageWithHashes() {
        val fileName = "testFile.txt"
        val commitSubject = "This is a subject line"
        val commitMessage = listOf(
                commitSubject,
                "",
                "#First body line",
                "#Second body line"
        )

        testFolder.newFile(fileName)
        val commandHelper = TestCommandHelper(testFolder.root)
        commandHelper.init().initConfig().addAll()

        littleGit.repoModifier.commit(commitMessage)

        val fullCommit = littleGit.repoReader.getFullCommit(commandHelper.getLastCommitHash()).data
        assertEquals(commitMessage, fullCommit?.commitBody)

        val fileDiffs = fullCommit?.diff?.fileDiffs
        assertEquals(1, fileDiffs?.size)
        assertTrue(fileDiffs?.get(0) is FileDiff.NewFile)

        val newFile = fileDiffs?.get(0) as FileDiff.NewFile
        assertEquals(fileName, newFile.filePath)
        assertEquals(emptyList<Hunk>(), newFile.hunks)
        assertEquals(commitSubject, fullCommit.commitSubject)
    }

    @Test fun test() {
        val core = LittleGitCore.Builder()
                .setRemoteHost("ec2-34-254-60-101.eu-west-1.compute.amazonaws.com")
                .setRemoteUser("ubuntu")
                .setRepoDirectoryPath("~/test.git")
                .build()

        val commit = core.repoReader.getFullCommit("ea71e4a141891c61a813a56ab20be55b59ed3774")
        println((commit.result as GitResult.Success).lines.joinWithNewLines())
    }
}