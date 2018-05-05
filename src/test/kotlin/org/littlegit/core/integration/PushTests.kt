package org.littlegit.core.integration

import org.junit.Test

class PushTests: BaseIntegrationTest() {

    @Test fun testNoRemote() {
        TestCommandHelper(testFolder.root).init()

    }
}