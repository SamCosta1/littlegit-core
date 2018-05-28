package org.littlegit.core.unit.resultparser

import org.junit.Rule
import org.junit.Test
import org.littlegit.core.helper.LocalResourceFile
import org.littlegit.core.parser.FullCommitParser
import junit.framework.TestCase.assertEquals
import org.littlegit.core.model.*
import java.time.Instant
import java.time.OffsetDateTime
import java.time.ZoneId

class FullCommitParserTests {

    @get:Rule val singleFileCreated = LocalResourceFile("full-commit-create-one-file.txt")
    @get:Rule val singleFileModified = LocalResourceFile("full-commit-modify-one-file.txt")

    @Test fun testSingleFileCreated() {
        val fullCommit = FullCommitParser.parse(singleFileCreated.content)

        val correctDateTime = OffsetDateTime.ofInstant(Instant.ofEpochMilli(1526744367000), ZoneId.systemDefault())
        val correctCommitMessage = """
        This is a commit message
        On branch master
        Changes to be committed:
        new file:   helloWorld.txt""".trimIndent()

        val fileContent = listOf(DiffLine(DiffLineType.Addition, null, 1, "hellow"))
        val fileDiff = NewFile("helloWorld.txt", listOf(Hunk(0,0,1,0, "", fileContent)))
        val diff = Diff(listOf(fileDiff))

        assertEquals(fullCommit, FullCommit("cd8a91b9dcaa85d993246bd408905650d464bfa5",
                                                listOf("refs/heads/master") ,
                                                listOf("d4ae6cdcba391e3c9f43dd9a5629427af5445911"),
                                                correctDateTime,
                                                "samuel.dacosta@student.manchester.ac.uk",
                                                "Commit subject",
                                                true,
                                                diff,
                                                correctCommitMessage))
    }

    @Test fun testSingleFileModified() {
        val fullCommit = FullCommitParser.parse(singleFileModified.content)

        val correctDateTime = OffsetDateTime.ofInstant(Instant.ofEpochMilli(1526208525000), ZoneId.systemDefault())
        val correctCommitMessage = """
        ignore ds store
        """.trimIndent()

        val fileContent = listOf(
            DiffLine(DiffLineType.Unchanged, 108, 108, ""),
            DiffLine(DiffLineType.Unchanged, 109, 109, ""),
            DiffLine(DiffLineType.Unchanged, 110, 110, "# End of https://www.gitignore.io/api/gradle,kotlin,intellij"),
            DiffLine(DiffLineType.Addition, null, 111, ""),
            DiffLine(DiffLineType.Addition, null, 112, "src/test/kotlin/org/littlegit/core/integration/\\.DS_Store")
        )

        val fileDiff = ChangedFile(".gitignore", listOf(Hunk(108,3,108,5, "gradle-app.setting", fileContent)))
        val diff = Diff(listOf(fileDiff))

        assertEquals(fullCommit, FullCommit("c2d902b4dffb0a65d1778bb5f057bac9bdb433dc",
                listOf("refs/remotes/origin/feature/git-show") ,
                listOf("e09f03aaf99b2d4b5e3ba72a8ccb7ca81e0c8e82"),
                correctDateTime,
                "samuel.dacosta@student.manchester.ac.uk",
                "ignore ds store",
                false,
                diff,
                correctCommitMessage))
    }
}