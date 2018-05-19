package org.littlegit.core.unit.resultparser

import junit.framework.TestCase.assertTrue
import org.junit.Rule
import org.junit.Test
import org.littlegit.core.model.GitError
import org.littlegit.core.commandrunner.GitResult
import org.littlegit.core.helper.LocalResourceFile
import org.littlegit.core.parser.GitResultParser
import org.littlegit.core.shell.ShellResult

class ResultParserTests {

    @get:Rule val localChangesError = LocalResourceFile("err-local-changes.txt")
    @get:Rule val notARepoError = LocalResourceFile("err-not-a-repo.txt")
    @get:Rule val nothingToCommit = LocalResourceFile("err-nothing-to-commit.txt")
    @get:Rule val noRemoteError = LocalResourceFile("err-no-remote.txt")
    @get:Rule val noUpstreamError = LocalResourceFile("err-no-upstream.txt")
    @get:Rule val cannotReadRemoteError = LocalResourceFile("err-cannot-read-remote.txt")
    @get:Rule val cannotReadRemoteHttpError = LocalResourceFile("err-cannot-read-remote-http.txt")

    @Test fun testLocalChangesWouldBeOverwritten() {
        val parsedResult = GitResultParser.parseShellResult(ShellResult.Error(localChangesError.content))
        assertTrue(parsedResult is GitResult.Error && parsedResult.err is GitError.LocalChangesWouldBeOverwritten)
    }

    @Test fun testNotARepo() {
        val parsedResult = GitResultParser.parseShellResult(ShellResult.Error(notARepoError.content))
        assertTrue(parsedResult is GitResult.Error && parsedResult.err is GitError.NotARepo)
    }

    @Test fun testNothingToCommit() {
        val parsedResult = GitResultParser.parseShellResult(ShellResult.Success(nothingToCommit.content))
        assertTrue(parsedResult is GitResult.Error && parsedResult.err is GitError.NothingToCommit)
    }

    @Test fun testNoRemoteBranch() {
        val parsedResult = GitResultParser.parseShellResult(ShellResult.Error(noRemoteError.content))
        assertTrue(parsedResult is GitResult.Error && parsedResult.err is GitError.NoRemote)
    }

    @Test fun testNoUpstreamBranch() {
        val parsedResult = GitResultParser.parseShellResult(ShellResult.Error(noUpstreamError.content))
        println(parsedResult)
        assertTrue(parsedResult is GitResult.Error && parsedResult.err is GitError.NoUpstreamBranch)
    }

    @Test fun testCannotReadRemote() {
        val parsedResult = GitResultParser.parseShellResult(ShellResult.Error(cannotReadRemoteError.content))
        assertTrue(parsedResult is GitResult.Error && parsedResult.err is GitError.CannotReadRemote)
    }

    @Test fun testCannotReadRemoteHttp() {
        val parsedResult = GitResultParser.parseShellResult(ShellResult.Error(cannotReadRemoteHttpError.content))
        assertTrue(parsedResult is GitResult.Error && parsedResult.err is GitError.CannotReadRemote)
    }

    @Test
    fun testEmptyError() {
        val parsedResult = GitResultParser.parseShellResult(ShellResult.Error(emptyList()))
        assertTrue("Result result is error",  parsedResult is GitResult.Error)

        val error = parsedResult as GitResult.Error
        assertTrue("Result result is error",  error.err is GitError.Unknown)
        assertTrue("lines are empty", error.err.error.isEmpty())
    }

    @Test
    fun testEmptySuccess() {
        val parsedResult = GitResultParser.parseShellResult(ShellResult.Success(emptyList()))
        assertTrue("Result result is success",  parsedResult is GitResult.Success)

        val success = parsedResult as GitResult.Success
        assertTrue("Result lines are empty",  success.lines.isEmpty())
    }

    @Test
    fun testBlankLinesError() {
        val parsedResult = GitResultParser.parseShellResult(ShellResult.Error(listOf("   ", "", " ")))
        assertTrue("Result result is error",  parsedResult is GitResult.Error)

        val error = parsedResult as GitResult.Error
        assertTrue("Result result is error",  error.err is GitError.Unknown)
        assertTrue("lines are empty", error.err.error.isEmpty())
    }

}