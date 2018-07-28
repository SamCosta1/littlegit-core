package org.littlegit.core.integration

import junit.framework.Assert.assertFalse
import junit.framework.Assert.assertTrue
import org.junit.Test
import org.littlegit.core.commandrunner.GitResult
import org.littlegit.core.helper.TestCommandHelper
import org.littlegit.core.model.GitError
import java.io.File

class StagingTests: BaseIntegrationTest() {
    private lateinit var commandHelper: TestCommandHelper

    override fun setup() {
        super.setup()
        commandHelper = TestCommandHelper(testFolder.root)
                            .init()
                            .initConfig()
    }

    @Test
    fun testStageExistingFile() {
        val fileName = "test.txt"

        val file = commandHelper.writeToFileAndReturnIt(fileName, "Some content")
        val result = littleGit.repoModifier.stageFile(file)

        assertTrue(result.result is GitResult.Success)
        assertTrue(commandHelper.isStaged(file))
    }

    @Test
    fun testStage1FileOf2() {
        val file1Name = "test.txt"
        val file2Name = "test2.txt"

        val file1 = commandHelper.writeToFileAndReturnIt(file1Name, "Some content")
        val file2 = commandHelper.writeToFileAndReturnIt(file2Name, "Some content")

        val result = littleGit.repoModifier.stageFile(file1)

        assertTrue(result.result is GitResult.Success)
        assertTrue(commandHelper.isStaged(file1))
        assertFalse(commandHelper.isStaged(file2))
    }

    @Test
    fun testStageNonExistentFile() {
        val fileName = "test.txt"

        val file = File("${testFolder.root.absolutePath}/$fileName")

        val gitResult = littleGit.repoModifier.stageFile(file).result
        assertFalse(file.exists())
        assertTrue(gitResult is GitResult.Error)

        gitResult as GitResult.Error
        assertTrue(gitResult.err is GitError.PathspecMatchesNoFiles)
    }

    fun testUnStageExistingFile() {
        val fileName = "test.txt"

        val file = commandHelper.writeToFileAndReturnIt(fileName, "Some content")

        littleGit.repoModifier.stageFile(file)
        assertTrue(commandHelper.isStaged(file))

        val result = littleGit.repoModifier.unStageFile(file)
        assertTrue(result.result is GitResult.Success)
        assertFalse(commandHelper.isStaged(file))
    }

    @Test
    fun testUnStageNonExistentFile() {
        val fileName = "test.txt"

        val file = File("${testFolder.root.absolutePath}/$fileName")

        val gitResult = littleGit.repoModifier.stageFile(file).result
        assertFalse(file.exists())
        assertTrue(gitResult is GitResult.Error)

        gitResult as GitResult.Error
        assertTrue(gitResult.err is GitError.PathspecMatchesNoFiles)
    }

}