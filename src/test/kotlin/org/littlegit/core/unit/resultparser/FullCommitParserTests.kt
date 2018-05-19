package org.littlegit.core.unit.resultparser

import org.junit.Rule
import org.junit.Test
import org.littlegit.core.helper.LocalResourceFile
import org.littlegit.core.parser.FullCommitParser

class FullCommitParserTests {

    @get:Rule val fileCreated = LocalResourceFile("full-commit-create-one-file.txt")

    @Test fun testFileCreated() {
        FullCommitParser.parse(fileCreated.content)
    }
}