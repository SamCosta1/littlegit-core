package org.littlegit.core.integration

import junit.framework.TestCase.*
import org.junit.Test
import org.littlegit.core.commandrunner.GitResult
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

    @Test fun testReadRemotes() {
        val remote1 = "remote1"
        val remote2 = "remote2"
        val remote1Url = "www.remote1.com"
        val remote2Url = "www.remote2.com"
        TestCommandHelper(testFolder.root).init()
                .addRemote(remote1, remote1Url)
                .addRemote(remote2, remote2Url)

        littleGit.repoReader.getRemotes { remotes, result ->
            assertTrue("Result is successful", result is GitResult.Success)
            assertNotNull("Remotes non null", remotes)
            assertEquals(2, remotes!!.size)

            assertTrue(remotes.contains(Remote(remote1, remote1Url, remote1Url)))
            assertTrue(remotes.contains(Remote(remote2, remote2Url, remote2Url)))

        }
    }

    @Test fun testReadNoRemotes() {
        TestCommandHelper(testFolder.root).init()

        littleGit.repoReader.getRemotes { remotes, result ->
            assertTrue("Result is successful", result is GitResult.Success)
            assertNotNull("Remotes non null", remotes)
            assertEquals(0, remotes!!.size)
        }
    }

}