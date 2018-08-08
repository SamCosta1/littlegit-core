package org.littlegit.core.integration

import org.junit.Before
import org.junit.Rule
import org.junit.rules.TemporaryFolder
import org.littlegit.core.LittleGitCore
import java.io.File
import java.nio.file.Paths

open class BaseIntegrationTest {

    @Rule
    @JvmField var testFolder = TemporaryFolder()

    lateinit var littleGit: LittleGitCore

    @Before
    open fun setup() {
        littleGit = LittleGitCore.Builder()
                        .setRepoDirectoryPath(testFolder.root.toPath())
                        .build()
    }

    protected fun fileInTestFolder(name: String): File = Paths.get(testFolder.root.canonicalPath, name).toFile()
}
