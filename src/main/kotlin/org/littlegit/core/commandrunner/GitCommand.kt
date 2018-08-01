package org.littlegit.core.commandrunner

import org.littlegit.core.model.FileDiff
import org.littlegit.core.model.Hunk
import java.io.File

typealias CommitHash = String
abstract class GitCommand {

    abstract val command: List<String>

    class ShowFile(val ref: String, val filePath: String): GitCommand() {
        override val command: List<String> get() = listOf("git", "show", "$ref:$filePath")
    }
    
    class IsInitialized : GitCommand() {
        override val command: List<String> get() = listOf("git", "rev-parse", "--is-inside-work-tree")
    }

    class InitializeRepo(val bare: Boolean, val name: String? = null) : GitCommand() {
        override val command: List<String> get()  {
            val commands = mutableListOf("git", "init")
            if (bare)         commands.add("--bare")
            if (name != null) commands.add(name)

            return commands
        }
    }

    class Commit(val commitFile: File) : GitCommand() {
        override val command: List<String> get() = listOf("git", "commit", "-F", commitFile.absolutePath)
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

    class PushSetUpstream(val remote: String, val branch: String): GitCommand() {
        override val command: List<String> get() = listOf("git", "push", "-u", remote, branch)
    }

    class AddRemote(val name: String = "origin", val url: String): GitCommand() {
        override val command: List<String> get() = listOf("git", "remote", "add", name, url)
    }

    class ListRemotes : GitCommand() {
        override val command: List<String> = listOf("git", "remote", "-vv")
    }

    class Log : GitCommand() {
        companion object {
            var deliminator = "@|@"
            //     | RawCommit hash | Parent Hashes | Refs |   Timestamp  | committer email | Subject line of message
            var format = "%H$deliminator%P$deliminator%D$deliminator%ct$deliminator%ce$deliminator%s"
            var formatWithBody = "$format%n%B%n%n"
        }

        override val command: List<String> get() = listOf("git", "log", "--all", "--decorate=full", "--format=\"$format\"")
    }

    class FullCommit(val commit: CommitHash) : GitCommand() {
        override val command: List<String> get() = listOf("git", "show", commit, "--date=iso", "--decorate=full", "--format=\"${Log.formatWithBody}\"")
    }

    class StageFile(val file: File): GitCommand() {
        override val command: List<String> get() = listOf("git", "add", file.absolutePath)
    }

    class UnStageFile(val file: File): GitCommand() {
        override val command: List<String> get() = listOf("git", "reset", file.absolutePath)
    }

    class StagingAreaDiff: GitCommand() {
        override val command: List<String> = listOf("git", "diff", "--cached")
    }

    class GetUnTrackedNonIgnoredFiles: GitCommand() {
        override val command: List<String> = listOf("git", "ls-files", "--exclude-standard", "--others")
    }

    class UnStagedDiff: GitCommand() {
        override val command: List<String> = listOf("git", "diff")
    }

    class ApplyPatch(patchFile: File) : GitCommand() {
        override val command: List<String> = listOf("git", "apply", "--cached", patchFile.canonicalPath)
    }

}
