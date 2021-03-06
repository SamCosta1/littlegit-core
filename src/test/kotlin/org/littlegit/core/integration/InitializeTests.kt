package org.littlegit.core.integration

import org.junit.Assert.*
import org.junit.Test
import org.littlegit.core.commandrunner.GitResult
import org.littlegit.core.helper.TestCommandHelper
import java.nio.file.Files
import java.nio.file.Paths

class InitializeTests: BaseIntegrationTest() {


    @Test fun testInitRepo() {
        val gitResult = littleGit.repoModifier.initializeRepo()
        assertTrue("Repo is initialized", gitResult.result is GitResult.Success)
        assertTrue(".git directory created", Files.exists(Paths.get("${testFolder.root.path}/.git")))
    }

    @Test fun testInitBareRepo() {
        val gitResult = littleGit.repoModifier.initializeRepo(bare = true)

        assertTrue("Repo is initialized", gitResult.result is GitResult.Success)
        assertTrue("repo initialised", Files.exists(Paths.get("${testFolder.root.path}/HEAD")))
    }

    @Test fun testInitBareRepoWithName() {
        val repoName = "testRepoName.git"
        val gitResult = littleGit.repoModifier.initializeRepo(bare = true, name = repoName)

        assertTrue("Repo is initialized", gitResult.result is GitResult.Success)
        assertFalse(".git directory created", Files.exists(Paths.get("${testFolder.root.path}/.git")))
        assertTrue("directory created", Files.exists(Paths.get("${testFolder.root.path}/$repoName")))
        assertTrue("repo initialised", Files.exists(Paths.get("${testFolder.root.path}/$repoName/HEAD")))
    }

    @Test fun testCheckRepoNotInitialized() {
        assertFalse("Directory not initially a git directory", Files.exists(Paths.get("${testFolder.root.path}/.git")))

        val isInitialized = littleGit.repoReader.isInitialized().data
        assertTrue("Repo not initialized", isInitialized == false)

    }

    @Test fun testCheckRepoInitialized() {
        assertFalse("Directory not initially a git directory", Files.exists(Paths.get("${testFolder.root.path}/.git")))

        TestCommandHelper(testFolder.root).init()

        val isInitialized = littleGit.repoReader.isInitialized().data
        assertTrue("Repo  initialized", isInitialized == true)
    }
}
