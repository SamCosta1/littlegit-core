package org.littlegit.core

import org.junit.Assert.assertEquals
import org.junit.Test
import org.littlegit.core.MyLibrary

class MyLibraryTest {
    @Test fun testMyLanguage() {
        assertEquals("Kotlin", MyLibrary().kotlinLanguage().name)
        assertEquals(10, MyLibrary().kotlinLanguage().hotness)
    }
}
