package org.littlegit.core.unit.resultparser

import org.junit.Rule
import org.junit.Test
import org.littlegit.core.helper.LocalResourceFile
import org.littlegit.core.parser.FullCommitParser
import junit.framework.TestCase.assertEquals
import org.littlegit.core.model.FullCommit
import java.time.Instant
import java.time.OffsetDateTime
import java.time.ZoneId

class FullCommitParserTests {

    @get:Rule val fileCreated = LocalResourceFile("full-commit-create-one-file.txt")

    @Test fun testFileCreated() {
        val fullCommit = FullCommitParser.parse(fileCreated.content)

        val correctDateTime = OffsetDateTime.ofInstant(Instant.ofEpochMilli(1526744367000), ZoneId.systemDefault())
        val correctCommitMessage = """
        This is a commit message
        On branch master
        Changes to be committed:
        new file:   helloWorld.txt""".trimIndent()

        assertEquals(fullCommit, FullCommit("cd8a91b9dcaa85d993246bd408905650d464bfa5",
                                                listOf("refs/heads/master") ,
                                                listOf("d4ae6cdcba391e3c9f43dd9a5629427af5445911"),
                                                correctDateTime,
                                                "samuel.dacosta@student.manchester.ac.uk",
                                                "Commit subject",
                                                true,
                                                correctCommitMessage))
    }
}