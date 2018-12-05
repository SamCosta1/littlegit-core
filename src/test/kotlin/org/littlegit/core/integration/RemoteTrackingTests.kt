package org.littlegit.core.integration

import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import org.junit.Assert.assertNull
import org.junit.Test
import org.littlegit.core.helper.TestCommandHelper
import org.littlegit.core.model.LocalBranch
import org.littlegit.core.model.RemoteBranch

class RemoteTrackingTests: BaseIntegrationTest() {

    lateinit var commandHelper: TestCommandHelper

    override fun setup() {
        super.setup()
        commandHelper = TestCommandHelper(testFolder.root)
                .init()
                .initConfig()
    }

    @Test
    fun testSetRemoteTrackingBranch_IsSuccessful() {
        val localBranchName = "local-branch"
        val remoteBranchName = "remote-branch"
        commandHelper.writeToFile("frodo_shoppinglist.txt", "Long bottom leaf")
                .addAll()
                .commit()
                .addRemote("origin")
                .createRemoteBranch(remoteBranchName, "origin")
                .branchAndCheckout(localBranchName)

        val branches = littleGit.repoReader.getBranches().data
        var localBranch = branches?.find { it.branchName == localBranchName } as LocalBranch
        val remoteBranch = branches.find { it.branchName == remoteBranchName } as RemoteBranch

        // Check the local branch isn't wired up to the remote yet
        assertNull(localBranch.upstream)

        val remoteResult = littleGit.repoModifier.setRemoteTracking(localBranch, remoteBranch)
        assertFalse(remoteResult.isError)
        localBranch = littleGit.repoReader.getBranches().data?.find { it.branchName == localBranchName } as LocalBranch
        assertEquals(remoteBranch, localBranch.upstream)
    }

}