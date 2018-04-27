package org.littlegit.core

import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import org.littlegit.core.shell.GitResult
import java.nio.file.Files
import java.nio.file.Paths

class InitializeTests {

    @Rule @JvmField var testFolder = TemporaryFolder()
    lateinit var littleGit: LittleGitCore

    @Before fun setup() {
        littleGit = LittleGitCore(testFolder.root.path)
    }

    @Test fun testInitRepo() {
        littleGit.repoModifier.initializeRepo { _, result ->
            assertTrue("Repo is initialized", result is GitResult.Success)
            assertTrue(".git directory created", Files.exists(Paths.get("${testFolder.root.path}/.git")))
        }
    }

    @Test fun testCheckRepoNotInitialized() {
        assertFalse("Directory not initially a git directory", Files.exists(Paths.get("${testFolder.root.path}/.git")))

        littleGit.repoReader.isInitialized { isInitialized, _ ->
            assertTrue("Repo not initialized", isInitialized == false)
        }
    }

    @Test fun testCheckRepoInitialized() {
        assertFalse("Directory not initially a git directory", Files.exists(Paths.get("${testFolder.root.path}/.git")))

        TestCommandHelper(testFolder.root).init()

        littleGit.repoReader.isInitialized { isInitialized, _ ->
            assertTrue("Repo  initialized", isInitialized == true)
        }
    }
}
