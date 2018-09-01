package org.littlegit.core.integration

import junit.framework.TestCase.*
import org.junit.Test
import org.littlegit.core.commandrunner.GitResult
import org.littlegit.core.helper.TestCommandHelper
import java.nio.file.Paths

class MergingTests: BaseIntegrationTest() {

    private lateinit var commandHelper: TestCommandHelper

    override fun setup() {
        super.setup()
        commandHelper = TestCommandHelper(testFolder.root)
                .init()
                .initConfig()
    }

    @Test
    fun testMergeNoConflicts() {
        val fileName = "minas-tirith.txt"
        val branchName = "gondor"

        commandHelper
                .writeToFile(fileName, "Yeah this isn't going great")
                .addAll()
                .commit()
                .branchAndCheckout(branchName)
                .writeToFile(fileName, "Calls for aid!")
                .addAll()
                .commit()
                .checkout("master")

        val branches = littleGit.repoReader.getBranches()
        val branch = branches.data?.find { it.branchName == branchName }

        assertNotNull(branch); branch!!
        val result = littleGit.repoModifier.merge(branch, false)
        assertTrue(result.result is GitResult.Success)
        assertFalse(result.data?.hasConflicts!!)
    }

    @Test
    fun testMergeWithConflicts() {
        val fileName = "minas-tirith.txt"
        val branchName = "gondor"

        commandHelper
                .writeToFile(fileName, "Yeah this isn't going great")
                .addAll()
                .commit()
                .branchAndCheckout(branchName)
                .writeToFile(fileName, "Calls for aid!")
                .addAll()
                .commit()
                .checkout("master")
                .writeToFile(fileName, "Defo going to go badly")
                .addAll()
                .commit()

        val branches = littleGit.repoReader.getBranches()
        val branch = branches.data?.find { it.branchName == branchName }

        assertNotNull(branch); branch!!
        val result = littleGit.repoModifier.merge(branch, false)
        assertTrue(result.result is GitResult.Success)
        assertTrue(result.data?.hasConflicts!!)

        val conflict = result.data?.conflictFiles?.first()
        assertEquals(1, result.data?.conflictFiles?.size)
        assertEquals(Paths.get(testFolder.root.canonicalPath, fileName), conflict?.filePath)
    }
}
