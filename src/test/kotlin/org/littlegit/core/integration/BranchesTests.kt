package org.littlegit.core.integration

import junit.framework.TestCase.*
import org.junit.Test
import org.littlegit.core.commandrunner.GitResult
import org.littlegit.core.helper.TestCommandHelper
import org.littlegit.core.model.LocalBranch

class BranchesTests: BaseIntegrationTest() {


    private lateinit var commandHelper: TestCommandHelper

    override fun setup() {
        super.setup()
        commandHelper = TestCommandHelper(testFolder.root)
                .init()
                .initConfig()
    }

    @Test
    fun testGetBranches() {

        val newBranch = "iron-hills-fixes"
        commandHelper
                .writeToFile("durin.txt", "Dwarves rule")
                .addAll()
                .commit()
                .branchAndCheckout(newBranch)

        val result = littleGit.repoReader.getBranches()
        assertTrue("Result was success", result.result is GitResult.Success)

        val branches = result.data
        assertNotNull(branches); branches!!
        assertEquals(2, branches.size)
        assertEquals("master", branches[0].branchName)
        assertEquals(newBranch, branches[1].branchName)
        assertEquals(commandHelper.getLastCommitHash(), branches[0].commitHash)
        assertEquals(commandHelper.getLastCommitHash(), branches[1].commitHash)
        assertFalse(branches[0].isHead)
        assertTrue(branches[1].isHead)
        assertTrue(branches[0] is LocalBranch)
        assertTrue(branches[1] is LocalBranch)
    }

    @Test
    fun testGetBranches_WhenNoneExist() {
        commandHelper.deleteBranch("master")

        val result = littleGit.repoReader.getBranches()
        assertTrue("Result was success", result.result is GitResult.Success)
        assertNotNull(result.data)
        assertEquals(0, result.data!!.size)
    }
}