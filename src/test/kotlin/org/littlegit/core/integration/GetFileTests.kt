package org.littlegit.core.integration

import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import org.junit.Test
import org.littlegit.core.commandrunner.GitResult
import org.littlegit.core.helper.TestCommandHelper
import org.littlegit.core.model.GitError

class GetFileTests: BaseIntegrationTest() {

    @Test fun lastCommitFileTest() {
        val testFileName = "file.txt"
        val content = "Ash nazg durbatulûk, ash nazg gimbatul,\n ash nazg thrakatulûk agh burzum-ishi krimpatul."

        TestCommandHelper(testFolder.root)
                .init()
                .writeToFile(testFileName, content)
                .addAll()
                .commit("Commit")

        littleGit.repoReader.getFile(file = testFileName) { file, result ->
            assertTrue("Result was success", result is GitResult.Success)
            assertEquals("Result file content is correct", file?.content, content.split("\n"))
            assertEquals("Result file name is correct", file?.filePath, testFileName)
        }
    }

    @Test fun testUnTrackedFile() {
        val testFileName = "file.txt"
        val content = "Ash nazg durbatulûk, ash nazg gimbatul,\n ash nazg thrakatulûk agh burzum-ishi krimpatul."

        TestCommandHelper(testFolder.root)
                .init()
                .writeToFile(testFileName, content)

        littleGit.repoReader.getFile(file = testFileName) { _, result ->
            assertTrue("Result was error", result is GitResult.Error)

            val error = result as GitResult.Error
            assertTrue("Result was correct error type", error.err is GitError.FileNotInIndex)
            assertTrue("File exists on disk", (error.err as GitError.FileNotInIndex).fileExistsOnDisk)
        }
    }

    @Test fun testNonExistingFile() {
        TestCommandHelper(testFolder.root).init()

        littleGit.repoReader.getFile(file = "somefile") { _, result ->
            assertTrue("Result was error", result is GitResult.Error)
            assertTrue("Result was correct error type", (result as GitResult.Error).err is GitError.FileNotInIndex)
            assertTrue("File exists on disk", !(result.err as GitError.FileNotInIndex).fileExistsOnDisk)
        }
    }

    @Test fun testOtherBranchVersion() {
        val testFileName = "file.txt"
        val branch = "branch"
        val content1 = "Ash nazg durbatulûk, ash nazg gimbatul,\n ash nazg thrakatulûk agh burzum-ishi krimpatul."
        val content2 = "One ring to rule them all"

        TestCommandHelper(testFolder.root)
                .init()
                .writeToFile(testFileName, content1)
                .addAll()
                .commit("Commit1")
                .branchAndCheckout(branch)
                .writeToFile(testFileName, content2)
                .addAll()
                .commit("Commit2")

        littleGit.repoReader.getFile("master", testFileName) { file, result ->
            assertTrue("Result was success", result is GitResult.Success)
            assertEquals("Result file content is correct", file?.content, content1.split("\n"))
            assertEquals("Result file name is correct", file?.filePath, testFileName)
        }

        littleGit.repoReader.getFile(branch, testFileName) { file, result ->
            assertTrue("Result was success", result is GitResult.Success)
            assertEquals("Result file content is correct", file?.content, content2.split("\n"))
            assertEquals("Result file name is correct", file?.filePath, testFileName)
        }
    }
}