package org.littlegit.core.integration

import junit.framework.TestCase.assertTrue
import org.junit.Test
import org.littlegit.core.commandrunner.GitResult
import org.littlegit.core.model.GitError

class AddRemoteTests: BaseIntegrationTest() {

    override fun setup() {
        super.setup()
        TestCommandHelper(testFolder.root).init()
    }

    @Test fun validAddRemoteTest() {
        littleGit.repoModifier.addRemote("origin", "www.remote.com") { _, result ->
            assertTrue("Adding remote was successful", result is GitResult.Success)
        }
    }

    @Test fun missingRemoteNameTest() {
        littleGit.repoModifier.addRemote("", "www.remote.com") { _, result ->
            assertTrue("Adding remote was unsuccessful", result is GitResult.Error)
            assertTrue("error should be invalid Remote name", result is GitResult.Error && result.err is GitError.InvalidRemoteName)
        }
    }

    @Test fun duplicateRemoteTest() {
        val remoteName = "origin"

        TestCommandHelper(testFolder.root).addRemote(remoteName)

        littleGit.repoModifier.addRemote(remoteName, "") { _, result ->
            assertTrue("Adding remote was unsuccessful", result is GitResult.Error)
            assertTrue("error should be remote already exists", result is GitResult.Error && result.err is GitError.RemoteAlreadyExists)
        }
    }
}