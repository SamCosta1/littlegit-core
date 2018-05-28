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

    @get:Rule val singleFileCreated = LocalResourceFile("fullCommits/full-commit-create-one-file.txt")
    @get:Rule val singleFileModified = LocalResourceFile("fullCommits/full-commit-modify-one-file.txt")
    @get:Rule val singleFileRenamed = LocalResourceFile("fullCommits/full-commit-rename-one-file.txt")
    @get:Rule val multipleFilesMultipleHunks = LocalResourceFile("fullCommits/full-commit-multiple-files-multiple-hunks.txt")

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

        assertEquals(FullCommit("cd8a91b9dcaa85d993246bd408905650d464bfa5",
                                                listOf("refs/heads/master") ,
                                                listOf("d4ae6cdcba391e3c9f43dd9a5629427af5445911"),
                                                correctDateTime,
                                                "samuel.dacosta@student.manchester.ac.uk",
                                                "Commit subject",
                                                true,
                                                diff,
                                                correctCommitMessage), fullCommit)
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

        assertEquals(FullCommit("c2d902b4dffb0a65d1778bb5f057bac9bdb433dc",
                listOf("refs/remotes/origin/feature/git-show") ,
                listOf("e09f03aaf99b2d4b5e3ba72a8ccb7ca81e0c8e82"),
                correctDateTime,
                "samuel.dacosta@student.manchester.ac.uk",
                "ignore ds store",
                false,
                diff,
                correctCommitMessage), fullCommit)
    }

    @Test fun testSingleFileRenamed() {
        val fullCommit = FullCommitParser.parse(singleFileRenamed.content)

        val correctDateTime = OffsetDateTime.ofInstant(Instant.ofEpochMilli(1527531388000), ZoneId.systemDefault())
        val correctCommitMessage = """
        Made the file into a js file""".trimIndent()

        val fileContent = listOf(
            DiffLine(DiffLineType.Unchanged, 1, 1, "function helloWorld() {"),
            DiffLine(DiffLineType.Deletion, 2, null, "   var hello = \"Hello world\";"),
            DiffLine(DiffLineType.Addition, null, 2, "   var hello = \"Hello world!!!!\";"),
            DiffLine(DiffLineType.Unchanged, 3, 3, "   console.log(hello);"),
            DiffLine(DiffLineType.Unchanged, 4, 4, "}"),
            DiffLine(DiffLineType.Unchanged, 5, 5, "")
        )

        val fileDiff = RenamedFile("helloworld.txt", "helloworld.js", listOf(Hunk(1,5,1,5, "", fileContent)))
        val diff = Diff(listOf(fileDiff))

        assertEquals(FullCommit("ee06970df7913f65dcd9a5e488d709177ec06e3f",
                listOf("refs/heads/master") ,
                listOf("9b0cac23347cf27fb2310d8765679fbd8fbcfa3c"),
                correctDateTime,
                "test@gmail.com",
                "Made the file into a js file",
                true,
                diff,
                correctCommitMessage), fullCommit)
    }

    @Test fun testMultipleFilesMultipleHunks() {
        val fullCommit = FullCommitParser.parse(multipleFilesMultipleHunks.content)

        val correctDateTime = OffsetDateTime.ofInstant(Instant.ofEpochMilli(1526047294000), ZoneId.systemDefault())
        val correctCommitMessage = """
        WIP: Adding remotes""".trimIndent()

        val file1Hunk1 = listOf(
            DiffLine(DiffLineType.Unchanged, 46, 46, "        override val command: List<String> get() = listOf(\"git\", \"remote\", \"add\", name, url)"),
            DiffLine(DiffLineType.Unchanged, 47, 47, "    }"),
            DiffLine(DiffLineType.Unchanged, 48, 48, ""),
            DiffLine(DiffLineType.Addition, null, 49, "    class ListRemotes : GitCommand() {"),
            DiffLine(DiffLineType.Addition, null, 50, "        override val command: List<String> get() = listOf(\"git\", \"remote\", \"-vv\")"),
            DiffLine(DiffLineType.Addition, null, 51, "    }"),
            DiffLine(DiffLineType.Addition, null, 52, ""),
            DiffLine(DiffLineType.Unchanged, 49, 53, "    class Log : GitCommand() {"),
            DiffLine(DiffLineType.Unchanged, 50, 54, "        companion object {"),
            DiffLine(DiffLineType.Unchanged, 51, 55, "            var deliminator = \"@|@\"")
        )

        val file1Hunk2 = listOf(
            DiffLine(DiffLineType.Unchanged, 55, 59, ""),
            DiffLine(DiffLineType.Unchanged, 56, 60, "        override val command: List<String> get() = listOf(\"git\", \"log\", \"--all\", \"--decorate=full\", \"--format=\$format\")"),
            DiffLine(DiffLineType.Unchanged, 57, 61, "    }"),
            DiffLine(DiffLineType.Addition, null, 62, ""),
            DiffLine(DiffLineType.Addition, null, 63, ""),
            DiffLine(DiffLineType.Unchanged, 58, 64, "}")
        )

        val file1Diff = ChangedFile("src/main/kotlin/org/littlegit/core/commandrunner/GitCommand.kt", listOf(
            Hunk(46,6,46,10, "abstract class GitCommand {", file1Hunk1),
            Hunk(55,4,59,6, "abstract class GitCommand {", file1Hunk2)
        ))

        val file2Content = listOf(
            DiffLine(DiffLineType.Addition, null, 1, "package org.littlegit.core.parser"),
            DiffLine(DiffLineType.Addition, null, 2, ""),
            DiffLine(DiffLineType.Addition, null, 3, "data class Remote(val remoteName: String, var pushUrl: String = \"\", var fetchUrl: String = \"\")"),
            DiffLine(DiffLineType.Addition, null, 4, ""),
            DiffLine(DiffLineType.Addition, null, 5, "class InvalidRemote(override var message: String = \"Remote is malformed\", raw: String): Exception(\"\$message: \$raw\")"),
            DiffLine(DiffLineType.Addition, null, 6, ""),
            DiffLine(DiffLineType.Addition, null, 7, "object RemoteParser {"),
            DiffLine(DiffLineType.NoNewLineAtEndOfFile, null, null, "\\ No newline at end of file")
        )

        val file2Diff = NewFile("src/main/kotlin/org/littlegit/core/parser/RemoteParser.kt", listOf(
            Hunk(0,0,1,7, "", file2Content)
        ))

        val diff = Diff(listOf(file1Diff, file2Diff))

        assertEquals(FullCommit("11dd41e66d4304469d61891a9d559002cfc24a4a",
                listOf() ,
                listOf("890982d2178b9b3b88f217560593447da54c8355"),
                correctDateTime,
                "samdc@apadmi.com",
                "WIP: Adding remotes",
                false,
                diff,
                correctCommitMessage).toString(), fullCommit.toString())
    }
}