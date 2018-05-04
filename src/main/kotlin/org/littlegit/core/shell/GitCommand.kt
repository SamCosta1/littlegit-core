package org.littlegit.core.shell

import org.littlegit.core.model.RawCommit
import java.time.Instant
import java.time.OffsetDateTime
import java.time.ZoneId


typealias CommitHash = String
abstract class GitCommand {

    abstract val command: List<String>

    class IsInitialized : GitCommand() {
        override val command: List<String> get() = listOf("git", "rev-parse", "--is-inside-work-tree")
    }

    class InitializeRepo : GitCommand() {
        override val command: List<String> get() = listOf("git", "init")
    }

    class Commit(val message: String) : GitCommand() {
        override val command: List<String> get() = listOf("git", "commit", "-m", message)
    }

    class Log : GitCommand() {
        private data class RefsResult(val refs: List<String>, val isHead: Boolean)
        companion object {
            private var deliminator = "@|@"

            //                   | RawCommit hash | Parent Hashes | Refs |   Timestamp  | committer email | Subject line of message
            private var format = "%H$deliminator%P$deliminator%D$deliminator%ct$deliminator%ce$deliminator%s"

            fun parse(rawLines: List<String>): List<RawCommit> {
                if (rawLines.isEmpty()) {
                    return emptyList()
                }

                val commits = mutableListOf<RawCommit>()

                rawLines.forEach {
                    val split = it.split(deliminator)

                    val commitHash = split[0]
                    val parentHashes = split[1].split(" ").filter { it.isNotBlank() }
                    val message = split.subList(5, split.size).joinToString(deliminator)

                    val date = OffsetDateTime.ofInstant(Instant.ofEpochMilli(split[3].toLong() * 1000), ZoneId.systemDefault())
                    val refResults = parseRef(split[2])
                    val committerEmail = split[4]

                    commits.add(RawCommit(commitHash, refResults.refs, parentHashes, date, committerEmail, message, refResults.isHead))
                }

                return commits.sortedByDescending { it.date }
            }

            private fun parseRef(rawRef: String): RefsResult {
                // Refs are split by spaces. But the first ref could be in the form HEAD -> [name]

                var split = rawRef.split(" ")
                var isHead = false

                if (split.size >= 2 && split[1] == "->") {
                    split = split.subList(2, split.size)
                    isHead = true
                }

                val formatted = mutableListOf<String>() // Refs with the trailing commas removed

                split.forEach {
                    val ref = it.removeSuffix(",")
                    if (ref.isNotBlank()) {
                        formatted.add(ref)
                    }
                }

                return RefsResult(formatted, isHead)
            }
        }

        override val command: List<String> get() = listOf("git", "log", "--all", "--decorate=full", "--format=$format")
    }
}
