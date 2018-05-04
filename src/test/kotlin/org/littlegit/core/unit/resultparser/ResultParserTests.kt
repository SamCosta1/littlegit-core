package org.littlegit.core.unit.resultparser

import junit.framework.TestCase.assertTrue
import org.junit.Test
import org.littlegit.core.shell.GitError
import org.littlegit.core.shell.GitResult
import org.littlegit.core.shell.GitResultParser
import org.littlegit.core.shell.ShellResult

class ResultParserTests {

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

    @Test
    fun testBlankLinesSuccess() {
        val parsedResult = GitResultParser.parseShellResult(ShellResult.Success(listOf("   ", "", " ")))
        assertTrue("Result result is success",  parsedResult is GitResult.Success)

        val success = parsedResult as GitResult.Success
        assertTrue("lines are empty", success.lines.isEmpty())
    }
}