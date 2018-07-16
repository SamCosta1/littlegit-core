package org.littlegit.core.integration

import org.junit.Before
import org.junit.Rule
import org.junit.rules.TemporaryFolder
import org.littlegit.core.LittleGitCore

open class BaseIntegrationTest {

    @Rule
    @JvmField var testFolder = TemporaryFolder()

    lateinit var littleGit: LittleGitCore

    @Before
    open fun setup() {
        littleGit = LittleGitCore.Builder()
                        .setRepoDirectoryPath(testFolder.root.path)
                        .build()
    }
}
