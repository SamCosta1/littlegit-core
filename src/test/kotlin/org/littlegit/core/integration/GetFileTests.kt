package org.littlegit.core.integration

import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import org.junit.Before
import org.junit.Test
import org.littlegit.core.commandrunner.GitResult
import org.littlegit.core.helper.TestCommandHelper
import org.littlegit.core.model.GitError

class GetFileTests: BaseIntegrationTest() {

    private lateinit var commandHelper: TestCommandHelper

    override fun setup() {
        super.setup()
        commandHelper = TestCommandHelper(testFolder.root)
                .init()
                .initConfig()
    }

    @Test fun lastCommitFileTest() {
        val testFileName = "file.txt"
        val content = "Ash nazg durbatulûk, ash nazg gimbatul,\n ash nazg thrakatulûk agh burzum-ishi krimpatul."

        commandHelper
                .writeToFile(testFileName, content)
                .addAll()
                .commit("Commit")

        val gitResult = littleGit.repoReader.getFile(file = testFileName)
            assertTrue("Result was success", gitResult.result is GitResult.Success)
            assertEquals("Result file content is correct", gitResult.data?.content, content.split("\n"))
            assertEquals("Result file name is correct", gitResult.data?.filePath, testFileName)

    }

    @Test fun testUnTrackedFile() {
        val testFileName = "file.txt"
        val content = "Ash nazg durbatulûk, ash nazg gimbatul,\n ash nazg thrakatulûk agh burzum-ishi krimpatul."

        commandHelper
                .writeToFile(testFileName, content)

        val gitResult = littleGit.repoReader.getFile(file = testFileName)
        assertTrue("Result was error", gitResult.result is GitResult.Error)

        val error = gitResult.result as GitResult.Error
        assertTrue("Result was correct error type", error.err is GitError.FileNotInIndex)
        assertTrue("File exists on disk", (error.err as GitError.FileNotInIndex).fileExistsOnDisk)

    }

    @Test fun testNonExistingFile() {
        val gitResult = littleGit.repoReader.getFile(file = "somefile")
        assertTrue("Result was error", gitResult.result is GitResult.Error)

        val err = (gitResult.result as GitResult.Error).err
        assertTrue("Result was correct error type", err is GitError.FileNotInIndex)

        err as GitError.FileNotInIndex
        assertTrue("File doesn't exists on disk", !err.fileExistsOnDisk)

    }

    @Test fun testOtherBranchVersion() {
        val testFileName = "file.txt"
        val branch = "branch"
        val content1 = "Ash nazg durbatulûk, ash nazg gimbatul,\n ash nazg thrakatulûk agh burzum-ishi krimpatul."
        val content2 = "One ring to rule them all"


        commandHelper
                .writeToFile(testFileName, content1)
                .addAll()
                .commit("Commit1")
                .branchAndCheckout(branch)
                .writeToFile(testFileName, content2)
                .addAll()
                .commit("Commit2")
        
        val branch1Res = littleGit.repoReader.getFile("master", testFileName)
        assertTrue("Result was success", branch1Res.result is GitResult.Success)
        assertEquals("Result file content is correct", branch1Res.data?.content, content1.split("\n"))
        assertEquals("Result file name is correct", branch1Res.data?.filePath, testFileName)

        val branch2Res = littleGit.repoReader.getFile(branch, testFileName)
        assertTrue("Result was success", branch1Res.result is GitResult.Success)
        assertEquals("Result file content is correct", branch2Res.data?.content, content2.split("\n"))
        assertEquals("Result file name is correct", branch2Res.data?.filePath, testFileName)
    }
}