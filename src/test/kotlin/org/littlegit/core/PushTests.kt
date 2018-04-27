package org.littlegit.core

import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder

class PushTests {
    @Rule @JvmField var testFolder = TemporaryFolder()
    lateinit var littleGit: LittleGitCore

    @Before fun setup() {
        littleGit = LittleGitCore(testFolder.root.path)
    }

    @Test fun testNoRemote() {
        TestCommandHelper(testFolder.root).init()

    }
}