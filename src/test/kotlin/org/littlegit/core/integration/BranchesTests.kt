package org.littlegit.core.integration

import junit.framework.TestCase.assertTrue
import org.junit.Test
import org.littlegit.core.commandrunner.GitResult
import org.littlegit.core.helper.TestCommandHelper

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

        commandHelper
                .writeToFile("durin.txt", "Dwarves rule")
                .addAll()
                .commit()
                .branchAndCheckout("iron-hills-fixes")

        val result = littleGit.repoReader.getBranches()
        assertTrue("Result was success", result.result is GitResult.Success)
    }

}