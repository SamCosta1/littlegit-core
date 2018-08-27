package org.littlegit.core.unit.parser

import junit.framework.TestCase.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import org.littlegit.core.helper.LocalResourceFile
import org.littlegit.core.model.ConflictFile
import org.littlegit.core.model.MergeResult
import org.littlegit.core.parser.ConflictFilesParser
import java.nio.file.Paths

@Suppress("MemberVisibilityCanBePrivate")
class ConflictFilesParserTests {

    @get:Rule
    val filesInSubDirs = LocalResourceFile("conflictFiles/files-in-subdirs.txt")

    @Test
    fun testWithFilesInSubDirs() {
        val dummyPath = "/Users/gandalf/Projects/toTheMountain/testFiles"
        val parsed = ConflictFilesParser.parse(Paths.get(dummyPath), filesInSubDirs.content)

        val expectedFile = ConflictFile(
                Paths.get(dummyPath, "subdir/sub.txt"),
                "30844b63e5f1bec58ef2ce01db1311d17699988e",
                "9f34a0f0645fa9afd22f02be37cc5031a079d308",
                "c64c940750ba8809f15c21250f1ed55bd4dcb95d")

        assertEquals(MergeResult(listOf(expectedFile)), parsed)
    }
}
