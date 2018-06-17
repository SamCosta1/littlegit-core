package org.littlegit.core.integration

import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import org.junit.Test
import org.littlegit.core.commandrunner.GitResult
import org.littlegit.core.helper.TestCommandHelper

class ConfigTests: BaseIntegrationTest() {

    @Test fun testUserName() {
        TestCommandHelper(testFolder.root).init()
        val testName = "Larry Smartington"
        littleGit.configModifier.setName(testName) { name, gitResult ->

            assertTrue("Result is successful", gitResult is GitResult.Success)
            assertEquals("Name matches", name, testName)

            littleGit.configModifier.getName { data, result ->
                assertTrue("Result is successful", result is GitResult.Success)
                assertEquals("Name matches", data, testName)
            }
        }
    }

    @Test fun testEmail() {
        TestCommandHelper(testFolder.root).init()
        val testEmail = "larry@scooby.com"

        littleGit.configModifier.setEmail(testEmail) { name, gitResult ->

            assertTrue("Result is successful", gitResult is GitResult.Success)
            assertEquals("Name matches", name, testEmail)

            littleGit.configModifier.getEmail { data, result ->
                assertTrue("Result is successful", result is GitResult.Success)
                assertEquals("Name matches", data, testEmail)
            }
        }
    }
}