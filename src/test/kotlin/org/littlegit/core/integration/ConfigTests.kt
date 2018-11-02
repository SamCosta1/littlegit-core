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
        val res = littleGit.configModifier.setName(testName)
        val name = res.data
        val setNameRes = res.result

        assertTrue("Result is successful", setNameRes is GitResult.Success)
        assertEquals("Name matches", name, testName)

        val getNameRes = littleGit.configModifier.getName()
        val retrievedName = getNameRes.data
        val retrievedResult = getNameRes.result

        assertTrue("Result is successful", retrievedResult is GitResult.Success)
        assertEquals("Name matches", retrievedName, testName)

    }

    @Test fun testEmail() {
        TestCommandHelper(testFolder.root).init()
        val testEmail = "larry@scooby.com"

        val setResult = littleGit.configModifier.setEmail(testEmail)
        assertTrue("Result is successful", setResult.result is GitResult.Success)
        assertEquals("Name matches", setResult.data, testEmail)

        val result = littleGit.configModifier.getEmail()
        assertTrue("Result is successful", result.result is GitResult.Success)
        assertEquals("Name matches", result.data, testEmail)
    }

    @Test fun testGetAndSetSshKeyPath() {
        TestCommandHelper(testFolder.root).init()
        val testPath = testFolder.root.resolve(".ssh").toPath()

        val setResult = littleGit.configModifier.setSshKeyPath(testPath)
        assertTrue("Result is successful", setResult.result is GitResult.Success)

        val getResult = littleGit.configModifier.getSshKeyPath()
        assertTrue("Result is successful", getResult.result is GitResult.Success)
        assertEquals("Paths match", testPath.normalize(), getResult.data!!.normalize())
    }

    @Test fun getSshKeyPathWithoutSet() {
        TestCommandHelper(testFolder.root).init()

        val getResult = littleGit.configModifier.getSshKeyPath()
        assertTrue("Result is successful", getResult.result is GitResult.Success)
        assertEquals("Path null", null, getResult.data)
    }
}