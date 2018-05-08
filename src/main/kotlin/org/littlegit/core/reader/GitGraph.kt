package org.littlegit.core.reader

import org.littlegit.core.model.RawCommit
import org.littlegit.core.commandrunner.CommitHash
import org.littlegit.core.util.weak
import java.lang.ref.WeakReference

typealias WeakNode = WeakReference<GraphNode?>
data class GraphNode(val commit: RawCommit, val children: MutableList<WeakNode> = mutableListOf(), val parents: MutableList<WeakNode> = mutableListOf())

class GitGraph(private val commits: List<RawCommit>): Iterable<GraphNode> {

    private val nodes: MutableMap<CommitHash, GraphNode> = mutableMapOf()
    private val initialCommits = mutableListOf<WeakNode>() // Nodes without parents, usually only 1
    private val endingCommits = mutableListOf<WeakNode>() // Nodes without children i.e. the ends of unmerged branches


    init {
        commits.forEach { commit ->
            nodes[commit.hash] = GraphNode(commit)
        }

        nodes.forEach { hash, node ->
            node.commit.parentHashes.forEach { parentHash ->
                val parent = nodes[parentHash]
                node.parents.add(parent.weak)
                parent?.children?.add(node.weak)
            }

            if (node.commit.parentHashes.isEmpty()) {
                initialCommits.add(node.weak)
            }
        }

        nodes.forEach { hash, node ->
            if (node.children.isEmpty()) {
                endingCommits.add(node.weak)
            }
        }
    }

    override fun iterator(): Iterator<GraphNode> {
        return object: Iterator<GraphNode> {
            private var nextIndex = 0
            override fun hasNext(): Boolean {
                return nextIndex < commits.size
            }

            override fun next(): GraphNode {
                return nodes[commits[nextIndex++].hash]!!
            }
        }
    }


    fun getInitialCommits() = initialCommits.toList()
    fun getEndingCommits() = endingCommits.toList()
    fun getNode(commit: RawCommit) = nodes[commit.hash]
    fun getNode(hash: CommitHash) = nodes[hash]
}
