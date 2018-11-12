package org.littlegit.core.integration

import junit.framework.TestCase.*
import org.junit.Test
import org.littlegit.core.commandrunner.GitResult
import org.littlegit.core.helper.TestCommandHelper
import org.littlegit.core.model.ConflictFileType
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

        val otherBranchContent = "Calls for aid!"
        val masterBranchContent = "Defo going to go badly"
        val baseContent = "Yeah this isn't going great"
        commandHelper
                .writeToFile(fileName, baseContent)
                .addAll()
                .commit()
                .branchAndCheckout(branchName)
                .writeToFile(fileName, otherBranchContent)
                .addAll()
                .commit()
                .checkout("master")
                .writeToFile(fileName, masterBranchContent)
                .addAll()
                .commit()

        val branches = littleGit.repoReader.getBranches()
        val branch = branches.data?.find { it.branchName == branchName }

        assertNotNull(branch); branch!!
        val result = littleGit.repoModifier.merge(branch, false)
        assertTrue(result.result is GitResult.Success)
        assertTrue(result.data?.hasConflicts!!)

        val conflict = result.data?.conflictFiles?.first()
        assertEquals(1, result.data?.conflictFiles?.size); conflict!!
        assertEquals(Paths.get(testFolder.root.canonicalPath, fileName), conflict.filePath)

        val theirsFile = littleGit.repoReader.getConflictFileContent(conflict, ConflictFileType.Theirs)
        assertEquals(conflict.filePath, theirsFile.data?.file?.toPath())
        assertEquals(listOf(otherBranchContent), theirsFile.data?.content)

        val oursFile = littleGit.repoReader.getConflictFileContent(conflict, ConflictFileType.Ours)
        assertEquals(conflict.filePath, oursFile.data?.file?.toPath())
        assertEquals(listOf(masterBranchContent), oursFile.data?.content)

        val baseFile = littleGit.repoReader.getConflictFileContent(conflict, ConflictFileType.Base)
        assertEquals(conflict.filePath, baseFile.data?.file?.toPath())
        assertEquals(listOf(baseContent), baseFile.data?.content)
    }
}
