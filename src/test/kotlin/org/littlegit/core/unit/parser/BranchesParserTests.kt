package org.littlegit.core.unit.parser

import junit.framework.TestCase.assertEquals
import org.junit.Rule
import org.junit.Test
import org.littlegit.core.helper.LocalResourceFile
import org.littlegit.core.model.LocalBranch
import org.littlegit.core.model.RemoteBranch
import org.littlegit.core.parser.BranchesParser
import org.littlegit.core.parser.Remote

@Suppress("MemberVisibilityCanBePrivate")
class BranchesParserTests {

    @get:Rule val refHeadAndRemotes = LocalResourceFile("refs/refs-with-head-and-remotes.txt")

    @Test
    fun testValidRef_WitHead_WithRemotes() {
        val expectedOriginRemote = Remote("origin")
        val expectedUpstreamRemote = Remote("upstream")
        val parsed = BranchesParser.parse(refHeadAndRemotes.content, listOf(expectedOriginRemote, expectedUpstreamRemote))

        val expectedOriginBranch1Remote = RemoteBranch("refs/remotes/origin/branch1", false, "ef2063e28a7c4c51b90c52537f0e6070424defd2", expectedOriginRemote)
        val expectedUpstreamBranch1Remote = RemoteBranch("refs/remotes/upstream/branch1", false, "ef2063e28a7c4c51b90c52537f0e6070424defd2", expectedUpstreamRemote)

        val expectedBranches = listOf(
            expectedUpstreamBranch1Remote,
            expectedOriginBranch1Remote,
            LocalBranch("refs/heads/master", false, null, "3e84683d9517a0b7106d776d58bdc946b8895650"),
            LocalBranch("refs/heads/branch2", false, null, "6cf952cb052928ff99d7b893c33545baec24465d"),
            LocalBranch("refs/heads/branch1", true, expectedOriginBranch1Remote, "b3b3755fb3c4b0757d43a6905af6ce043a8fcc30")
        )

        assertEquals(expectedBranches.size, parsed.size)
        assertEquals(expectedBranches.sortedBy { it.fullRefName }, parsed.sortedBy { it.fullRefName })
    }
}