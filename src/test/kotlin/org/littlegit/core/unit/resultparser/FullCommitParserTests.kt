package org.littlegit.core.unit.resultparser

import org.junit.Test
import org.littlegit.core.integration.BaseIntegrationTest
import org.littlegit.core.integration.TestCommandHelper
import org.littlegit.core.shell.ShellResult
import org.littlegit.core.shell.ShellRunner

class FullCommitParserTests: BaseIntegrationTest() {

    @Test fun test() {
        TestCommandHelper(testFolder.root).init()
    }
}