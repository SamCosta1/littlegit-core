package org.littlegit.core.integration

import junit.framework.TestCase.*
import org.junit.Test
import org.littlegit.core.commandrunner.GitResult
import org.littlegit.core.helper.TestCommandHelper
import org.littlegit.core.model.GitError
import org.littlegit.core.parser.Remote

class PushTests: BaseIntegrationTest() {

    @Test fun testNoRemote() {
        TestCommandHelper(testFolder.root).init()

        littleGit.repoModifier.push { _, result ->
            assertTrue("Result is error", result is GitResult.Error)
            assertTrue("Result error is no remote", (result as GitResult.Error).err is GitError.NoRemote)
        }
    }

    @Test(expected = IllegalArgumentException::class)
    fun testInvalidArgument() {
        TestCommandHelper(testFolder.root).init()

        littleGit.repoModifier.push(null, "", true)
    }
}