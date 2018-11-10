package org.littlegit.core.integration

import junit.framework.TestCase.assertFalse
import org.junit.Test
import org.littlegit.core.helper.TestCommandHelper

class FetchTests: BaseIntegrationTest() {

    private lateinit var commandHelper: TestCommandHelper

    override fun setup() {
        super.setup()
        commandHelper = TestCommandHelper(testFolder.root)
                .init()
                .initConfig()
    }

    // Very little that can really be tested here without real remotes
    @Test
    fun testFetch() {
        assertFalse(littleGit.repoModifier.fetch().isError)
    }

    @Test
    fun testQuietFetch() {
        assertFalse(littleGit.repoModifier.fetch(quiet = true).isError)
    }
}