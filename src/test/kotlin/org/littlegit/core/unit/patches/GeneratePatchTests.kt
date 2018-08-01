package org.littlegit.core.unit.patches

import junit.framework.TestCase.assertEquals
import org.junit.Rule
import org.junit.Test
import org.littlegit.core.helper.LocalResourceFile
import org.littlegit.core.model.FileDiff
import org.littlegit.core.parser.DiffParser

@Suppress("MemberVisibilityCanBePrivate")
class GeneratePatchTests {
    @get:Rule val multipleFilesMultipleHunks = LocalResourceFile("diffCommits/diff-commit-multiple-files-multiple-hunks.txt")

    @Test
    fun testHunkPatch() {

        val diff = DiffParser.parse(multipleFilesMultipleHunks.content)
        val firstFile = diff.fileDiffs.first() as FileDiff.ChangedFile

        val patch = firstFile.hunks[1].generatePatch(firstFile)
        val expectedPatch = """
        diff --git a/src/main/kotlin/org/littlegit/core/commandrunner/GitCommand.kt b/src/main/kotlin/org/littlegit/core/commandrunner/GitCommand.kt
        --- a/src/main/kotlin/org/littlegit/core/commandrunner/GitCommand.kt
        +++ b/src/main/kotlin/org/littlegit/core/commandrunner/GitCommand.kt
        @@ -55,4 +59,6 @@
        ${" "}
                 override val command: List<String> get() = listOf("git", "log", "--all", "--decorate=full", "--format=${"$"}format")
             }
        +
        +
         }
        """.trimIndent().split("\n").toMutableList()
        assertEquals(expectedPatch, patch)
    }

    @Test
    fun testReverseHunkPatch() {

        val diff = DiffParser.parse(multipleFilesMultipleHunks.content)
        val firstFile = diff.fileDiffs.first() as FileDiff.ChangedFile

        val patch = firstFile.hunks[1].generateInversePatch(firstFile)
        val expectedPatch = """
        diff --git a/src/main/kotlin/org/littlegit/core/commandrunner/GitCommand.kt b/src/main/kotlin/org/littlegit/core/commandrunner/GitCommand.kt
        --- a/src/main/kotlin/org/littlegit/core/commandrunner/GitCommand.kt
        +++ b/src/main/kotlin/org/littlegit/core/commandrunner/GitCommand.kt
        @@ -59,6 +55,4 @@
        ${" "}
                 override val command: List<String> get() = listOf("git", "log", "--all", "--decorate=full", "--format=${"$"}format")
             }
        -
        -
         }
        """.trimIndent().split("\n").toMutableList()
        assertEquals(expectedPatch, patch)
    }


}

