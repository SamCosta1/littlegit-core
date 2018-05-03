package org.littlegit.core.unit.resultparser

import junit.framework.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.littlegit.core.helper.ResourceFile
import org.littlegit.core.model.RawCommit
import org.littlegit.core.shell.GitCommand
import java.time.Instant
import java.time.OffsetDateTime
import java.time.ZoneId

class LogParsingTests {

    @get:Rule
    val smallLog = ResourceFile(System.getProperty("user.dir") + "/src/test/kotlin/testFiles/smallLog.txt")

    @get:Rule
    val emptyEmail = ResourceFile(System.getProperty("user.dir") + "/src/test/kotlin/testFiles/emptyEmail.txt")

    @Test fun specialCharacterCommitSubjectTest() {
        val parsed = GitCommand.Log.parse(smallLog.content)

        val correctCommits = listOf(RawCommit("428d62f5b7244454edfbf94fd99dee0972f130e8",
                                                    listOf("refs/heads/master"),
                                                    listOf("159a78f59031cb814f83148d6f6aaebd2a186a22", "bb7058bb7e9214f2497336938ce2e4a9c43ca96d"),
                                                    OffsetDateTime.ofInstant(Instant.ofEpochMilli(1525272325000), ZoneId.systemDefault()),
                                                    "samdc@apadmi.com",
                                                    "Merge branch 'feature'", true),
                                    RawCommit("bb7058bb7e9214f2497336938ce2e4a9c43ca96d",
                                                    emptyList(),
                                                    listOf("159a78f59031cb814f83148d6f6aaebd2a186a22"),
                                                    OffsetDateTime.ofInstant(Instant.ofEpochMilli(1525272312000), ZoneId.systemDefault()),
                                                    "test@email.com",
                                                    "feature comm!@£\$%^&*()_+)@£\$%^&*()(*&^%''\"\$£|||it", false),

                                    RawCommit("159a78f59031cb814f83148d6f6aaebd2a186a22",
                                                    emptyList(),
                                                    listOf("03e6c7df90e56aa5d721a14f8e8363397f17cc28"),
                                                    OffsetDateTime.ofInstant(Instant.ofEpochMilli(1525272270000), ZoneId.systemDefault()),
                                                    "samdc@apadmi.com",
                                                    "master commit 3", false),
                                    RawCommit("03e6c7df90e56aa5d721a14f8e8363397f17cc28",
                                                    emptyList(),
                                                    emptyList(),
                                                    OffsetDateTime.ofInstant(Instant.ofEpochMilli(1525272249000), ZoneId.systemDefault()),
                                                    "samdc@apadmi.com",
                                                    "master commit 2", false)
                                    )

        parsed.forEachIndexed { index, rawCommit ->
            assertTrue("Commit is as expected", rawCommit == correctCommits[index])
        }
    }

    @Test fun emptyEmailTest() {
        val parsed = GitCommand.Log.parse(emptyEmail.content)

        val correctCommits = listOf(
                RawCommit("03e6c7df90e56aa5d721a14f8e8363397f17cc28",
                        emptyList(),
                        emptyList(),
                        OffsetDateTime.ofInstant(Instant.ofEpochMilli(1525272249000), ZoneId.systemDefault()),
                        "",
                        "master commit 2", false)
        )

        parsed.forEachIndexed { index, rawCommit ->
            assertTrue("Commit is as expected", rawCommit == correctCommits[index])
        }
    }
}
