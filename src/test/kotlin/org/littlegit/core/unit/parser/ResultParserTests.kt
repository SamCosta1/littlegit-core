package org.littlegit.core.unit.parser

import junit.framework.TestCase.assertTrue
import org.junit.Rule
import org.junit.Test
import org.littlegit.core.model.GitError
import org.littlegit.core.commandrunner.GitResult
import org.littlegit.core.helper.LocalResourceFile
import org.littlegit.core.parser.GitResultParser
import org.littlegit.core.shell.ShellResult

@Suppress("MemberVisibilityCanBePrivate")
class ResultParserTests {

    @get:Rule val localChangesError = LocalResourceFile("err/err-local-changes.txt")
    @get:Rule val notARepoError = LocalResourceFile("err/err-not-a-repo.txt")
    @get:Rule val nothingToCommit = LocalResourceFile("err/err-nothing-to-commit.txt")
    @get:Rule val noRemoteError = LocalResourceFile("err/err-no-remote.txt")
    @get:Rule val noUpstreamError = LocalResourceFile("err/err-no-upstream.txt")
    @get:Rule val cannotReadRemoteError = LocalResourceFile("err/err-cannot-read-remote.txt")
    @get:Rule val cannotReadRemoteHttpError = LocalResourceFile("err/err-cannot-read-remote-http.txt")
    @get:Rule val invalidRemoteName = LocalResourceFile("err/err-invalid-remote-windows.txt")
    @get:Rule val lockedCommit = LocalResourceFile("err/err-head-locked.txt")
    @get:Rule val corruptPatchError = LocalResourceFile("err/err-corrupt-patch.txt")

    @Test fun testLockedCommit() {
        val parsedResult = GitResultParser.parseShellResult(ShellResult.Error(lockedCommit.content))
        assertTrue(parsedResult is GitResult.Error && parsedResult.err is GitError.CannotLockRef)
    }
    
    @Test fun testInvalidRemoteNameWindowsResult() {
        val parsedResult = GitResultParser.parseShellResult(ShellResult.Error(invalidRemoteName.content))
        assertTrue(parsedResult is GitResult.Error && parsedResult.err is GitError.InvalidRemoteInfo)
    }

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

    @Test fun testCorruptPatch() {
        val parsedResult = GitResultParser.parseShellResult(ShellResult.Error(corruptPatchError.content))
        assertTrue(parsedResult is GitResult.Error && parsedResult.err is GitError.CorruptPatch)
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