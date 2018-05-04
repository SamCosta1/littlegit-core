package org.littlegit.core.integration

import org.junit.Test

class PushTests: IntegrationBaseTest() {

    @Test fun testNoRemote() {
        TestCommandHelper(testFolder.root).init()

    }
}