package org.littlegit.core.commandrunner


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

    class SetUserEmail(val email: String, val global: Boolean = false) : GitCommand() {
        override val command: List<String> get() = if (global) listOf("git", "config", "--global", "user.email", email) else listOf("git", "config", "user.email", email)
    }

    class SetUserName(val name: String, val global: Boolean = false) : GitCommand() {
        override val command: List<String> get() = if (global) listOf("git", "config", "--global", "user.name", name) else listOf("git", "config", "user.name", name)
    }

    class GetUserName(val global: Boolean = false): GitCommand() {
        override val command: List<String> get() = if (global) listOf("git", "config", "--global", "user.name") else listOf("git", "config", "user.name")
    }

    class GetUserEmail(val global: Boolean = false): GitCommand() {
        override val command: List<String> get() = if (global) listOf("git", "config", "--global", "user.email") else listOf("git", "config", "user.email")
    }

    class Push(val remote: String? = null, val branch: String? = null): GitCommand() {
        override val command: List<String> get() = listOf("git", "push") + if (remote != null && branch != null) listOf(remote, branch) else emptyList()
    }

    class Log : GitCommand() {
        companion object {
            var deliminator = "@|@"
            //             | RawCommit hash | Parent Hashes | Refs |   Timestamp  | committer email | Subject line of message
            var format = "%H${deliminator}%P${deliminator}%D${deliminator}%ct${deliminator}%ce${deliminator}%s"
        }

        override val command: List<String> get() = listOf("git", "log", "--all", "--decorate=full", "--format=${format}")
    }
}
