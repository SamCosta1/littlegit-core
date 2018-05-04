package org.littlegit.core.shell


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

    class SetEmail(val email: String) : GitCommand() {
        override val command: List<String> get() = listOf("git", "config", "user.email", email)
    }

    class SetName(val name: String) : GitCommand() {
        override val command: List<String> get() = listOf("git", "config", "user.name", name)
    }

    class Log : GitCommand() {
        companion object {
            var deliminator = "@|@"
            //             | RawCommit hash | Parent Hashes | Refs |   Timestamp  | committer email | Subject line of message
            var format = "%H$deliminator%P$deliminator%D$deliminator%ct$deliminator%ce$deliminator%s"
        }

        override val command: List<String> get() = listOf("git", "log", "--all", "--decorate=full", "--format=$format")
    }
}
