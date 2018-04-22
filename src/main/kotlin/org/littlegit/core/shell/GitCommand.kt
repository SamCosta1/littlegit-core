package org.littlegit.core.shell

import org.littlegit.core.model.Commit
import java.time.Instant
import java.time.OffsetDateTime
import java.time.ZoneId

abstract class GitCommand {
    abstract val command: List<String>
}

class GitCommit(val message: String): GitCommand() {
    override val command: List<String> get() = listOf("git", "commit ", "-m", message)
}

class GitLog(): GitCommand() {
    companion object {
        private var deliminator = "|"
        private var format = "%H$deliminator%P$deliminator%d$deliminator%ct$deliminator%ce$deliminator%B"

        fun parse(rawLines: List<String>): List<Commit> {
            val commits = emptyList<Commit>()

            rawLines.forEach {
                val split = it.split(deliminator)

                val message = split.subList(6, split.size).joinToString(deliminator)
                val date = OffsetDateTime.ofInstant(Instant.ofEpochMilli(split[3].toLong()), ZoneId.systemDefault())
                val refs = parseRef(split[2])
                Commit(split[0], split[1].split(" "), refs.first, date, split[4], message, refs.second)
            }

            return commits
        }

        private fun parseRef(rawRef: String): Pair<List<String>, Boolean> {
            // Refs are split by spaces. But the first ref could be in the form HEAD -> [name]

            var split = rawRef.split(" ")
            var isHead = false

            if (split[1] == "->") {
                split = split.subList(2, split.size)
                isHead = true
            }

            val formatted = mutableListOf<String>() // Refs with the trailing commas removed
            split.forEach { formatted.add(it.removeSuffix(",")) }

            return Pair(formatted, isHead)
        }
    }

    override val command: List<String> get() = listOf("git", "log", "--all", "--decorate=full", "--format=\"$format\"")
}
