package org.littlegit.core.integration

import junit.framework.TestCase.assertEquals
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
        val remoteName = "origin"
        val remoteUrl = "www.remote.com"
        littleGit.repoModifier.addRemote("origin", remoteUrl) { _, result ->
            assertTrue("Adding remote was successful", result is GitResult.Success)

            littleGit.repoReader.getRemotes { remotes, res->
                assertTrue("Getting remotes was successful", res is GitResult.Success)
                assertTrue("One remote found", remotes?.size == 1)
                assertEquals("name correct", remotes?.get(0)?.remoteName, remoteName)
                assertEquals("push url correct", remotes?.get(0)?.pushUrl, remoteUrl)
                assertEquals("fetch url correct", remotes?.get(0)?.fetchUrl, remoteUrl)
            }
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