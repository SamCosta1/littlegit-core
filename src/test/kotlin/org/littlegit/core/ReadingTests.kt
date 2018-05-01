package org.littlegit.core

import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder

class ReadingTests {
    @Rule
    @JvmField var testFolder = TemporaryFolder()
    lateinit var littleGit: LittleGitCore

    @Before
    fun setup() {
        littleGit = LittleGitCore("/Users/samdc/personal/littlegit-core")
    }
    @Test fun test() {

        littleGit.repoReader.getGraph { gitGraph, gitResult ->
            println("Ending Commits: " + gitGraph?.getEndingCommits())
            println("Initial Commits: " + gitGraph?.getInitialCommits())
        }
    }
}