package org.littlegit.core.integration

import junit.framework.TestCase.*
import org.junit.Test
import org.littlegit.core.commandrunner.GitResult
import org.littlegit.core.helper.TestCommandHelper
import org.littlegit.core.model.GitError
import org.littlegit.core.model.LocalBranch
import org.littlegit.core.util.ListUtils
import java.nio.file.Paths

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

    @Test
    fun testCreateBranch() {
        commandHelper
                .writeToFile("map.txt", "Route to gimli")
                .addAll()
                .commit()

        val branchName = "find-gimli"
        val createResult = littleGit.repoModifier.createBranch(branchName)
        assertTrue("Result was success", createResult.result is GitResult.Success)

        val branches = littleGit.repoReader.getBranches()
        val branch = branches.data?.find { it.branchName == branchName }

        assertTrue(branch != null)
        assertTrue(branch is LocalBranch)
    }

    @Test
    fun testCreateBranch_FullRefName() {
        commandHelper
                .writeToFile("map.txt", "Route to gimli")
                .addAll()
                .commit()

        val branchName = "find-gimli"
        val fullRefName = "refs/heads/$branchName"

        val createResult = littleGit.repoModifier.createBranch(fullRefName)
        assertTrue("Result was success", createResult.result is GitResult.Success)

        val branches = littleGit.repoReader.getBranches()
        val branch = branches.data?.find { it.branchName == branchName }

        assertTrue(branch != null)
        assertTrue(branch is LocalBranch)
    }

    @Test
    fun testCreateBranch_AlreadyExists() {
        val branchName = "find-gimli"

        commandHelper
                .writeToFile("map.txt", "Route to gimli")
                .addAll()
                .commit()
                .branchAndCheckout(branchName)

        val result = littleGit.repoModifier.createBranch(branchName).result
        assertTrue("Result was error", result is GitResult.Error); result as GitResult.Error
        assertTrue("Result was error", result.err is GitError.ReferenceAlreadyExists)
    }

    @Test
    fun testCreateBranch_EmptyRepo() {
        val branchName = "find-gimli"
        val result = littleGit.repoModifier.createBranch(branchName).result
        assertTrue("Result was error", result is GitResult.Error); result as GitResult.Error
        assertTrue("Result was error", result.err is GitError.InvalidHead)
    }

    @Test
    fun testCreateBranch_AtSpecifiedCommit() {
        val branchName = "find-gimli"

        val lastCommitHash = commandHelper
                                .writeToFile("map.txt", "Route to gimli")
                                .addAll()
                                .commit()
                                .writeToFile("map2.txt", "Escape route once we've got him")
                                .addAll()
                                .commit()
                                .getLastCommitHash()

        val commits = littleGit.repoReader.getCommitList().data!!

        // Create a branch off he first commit
        val result = littleGit.repoModifier.createBranch(branchName, commit = commits.first()).result
        assertTrue("Result was success", result is GitResult.Success)

        val branches = littleGit.repoReader.getBranches()
        val branch = branches.data?.find { it.branchName == branchName }

        assertTrue(branch != null)
        assertTrue(branch is LocalBranch)
        assertEquals(commits.first().hash, branch?.commitHash)
        assertNotSame(lastCommitHash, commits.first().hash)
    }

    @Test
    fun testCreateBranch_InvalidBranchName() {
        val branchName = "find-gimli:invalid"

        commandHelper
                .writeToFile("map.txt", "Route to gimli")
                .addAll()
                .commit()
                .branchAndCheckout(branchName)

        val result = littleGit.repoModifier.createBranch(branchName).result
        assertTrue("Result was error", result is GitResult.Error); result as GitResult.Error
        assertTrue("Result was error", result.err is GitError.InvalidRefName)
    }

    @Test
    fun testCheckoutBranch() {
        val branchName = "find-gimli"

        val branchContent = "Rout to gimli via mines of moria"
        val masterContent = "Route to gimli"

        val file = commandHelper.writeToFileAndReturnIt("map.txt", masterContent)

        commandHelper
                .addAll()
                .commit()
                .branchAndCheckout(branchName)
                .writeToFile(file.name, branchContent)
                .checkout("master")

        val branches = littleGit.repoReader.getBranches()

        // Ensure we're currently on master
        assertTrue(branches.data!!.find { it.isHead }?.branchName == "master")

        val branchToCheckout = branches.data?.find { it.branchName == branchName }
        val result = littleGit.repoModifier.checkoutBranch(branchToCheckout as LocalBranch)

        assertTrue("Result was success", result.result is GitResult.Success)

        val newBranches = littleGit.repoReader.getBranches()
        assertEquals(branchName, newBranches.data?.find { it.isHead }?.branchName)
        assertEquals(branchContent, ListUtils.readFromPath(file.toPath()).first())
    }
}