package org.littlegit.core.integration

import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertTrue
import org.junit.Test
import org.littlegit.core.commandrunner.GitResult
import org.littlegit.core.helper.TestCommandHelper
import org.littlegit.core.model.FileDiff

class DiffTests: BaseIntegrationTest() {

    private lateinit var commandHelper: TestCommandHelper

    override fun setup() {
        super.setup()
        commandHelper = TestCommandHelper(testFolder.root)
                .init()
                .initConfig()
    }

    @Test
    fun testGetStagingArea_NoStagedChanges() {
        commandHelper.writeToFile("testFile.txt", "Some text")

        val result = littleGit.repoReader.getStagingAreaDiff()
        assertTrue(result.result is GitResult.Success)
        assertTrue(result.data?.fileDiffs?.isEmpty() == true)
    }

    @Test
    fun testGetStagingArea() {
        val stagedFileName = "stagedFile.txt"
        val stagedFileContent = "Mount doom"
        commandHelper
                .writeToFile(stagedFileName, stagedFileContent)
                .addAll()
                .writeToFile("unStagedFile.txt", "More text")

        val result = littleGit.repoReader.getStagingAreaDiff()
        assertTrue(result.result is GitResult.Success)
        assertEquals(1, result.data?.fileDiffs?.size)

        val fileDiff = result.data?.fileDiffs?.first()
        assertTrue(fileDiff is FileDiff.NewFile)
        fileDiff as FileDiff.NewFile

        assertEquals(stagedFileName, fileDiff.filePath)
        assertEquals(1, fileDiff.hunks.size)
        assertEquals(1, fileDiff.hunks.first().lines.size)
        assertEquals(stagedFileContent, fileDiff.hunks.first().lines.first().line)
    }
}