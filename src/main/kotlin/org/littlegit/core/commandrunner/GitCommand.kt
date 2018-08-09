package org.littlegit.core.commandrunner

import org.littlegit.core.model.FileDiff
import org.littlegit.core.model.Hunk
import java.io.File

typealias CommitHash = String
abstract class GitCommand {

    abstract val command: List<String>

    class ShowFile(private val ref: String, private val filePath: String): GitCommand() {
        override val command: List<String> get() = listOf("git", "show", "$ref:$filePath")
    }
    
    class IsInitialized : GitCommand() {
        override val command: List<String> get() = listOf("git", "rev-parse", "--is-inside-work-tree")
    }

    class InitializeRepo(private val bare: Boolean, val name: String? = null) : GitCommand() {
        override val command: List<String> get()  {
            val commands = mutableListOf("git", "init")
            if (bare)         commands.add("--bare")
            if (name != null) commands.add(name)

            return commands
        }
    }

    class Commit(private val commitFile: File) : GitCommand() {
        override val command: List<String> get() = listOf("git", "commit", "-F", commitFile.canonicalPath)
    }

    class SetUserEmail(private val email: String, private val global: Boolean = false) : GitCommand() {
        override val command: List<String> get() = if (global) listOf("git", "config", "--global", "user.email", email) else listOf("git", "config", "user.email", email)
    }

    class SetUserName(val name: String, private val global: Boolean = false) : GitCommand() {
        override val command: List<String> get() = if (global) listOf("git", "config", "--global", "user.name", name) else listOf("git", "config", "user.name", name)
    }

    class GetUserName(private val global: Boolean = false): GitCommand() {
        override val command: List<String> get() = if (global) listOf("git", "config", "--global", "user.name") else listOf("git", "config", "user.name")
    }

    class GetUserEmail(private val global: Boolean = false): GitCommand() {
        override val command: List<String> get() = if (global) listOf("git", "config", "--global", "user.email") else listOf("git", "config", "user.email")
    }

    class Push(val remote: String? = null, val branch: String? = null): GitCommand() {
        override val command: List<String> get() = listOf("git", "push") + if (remote != null && branch != null) listOf(remote, branch) else emptyList()
    }

    class PushSetUpstream(val remote: String, val branch: String): GitCommand() {
        override val command: List<String> get() = listOf("git", "push", "-u", remote, branch)
    }

    class AddRemote(val name: String = "origin", private val url: String): GitCommand() {
        override val command: List<String> get() = listOf("git", "remote", "add", name, url)
    }

    class ListRemotes : GitCommand() {
        override val command: List<String> = listOf("git", "remote", "-vv")
    }

    class SymbolicRef(val symRefName: String) : GitCommand() {
        override val command: List<String> = listOf("git", "symbolic-ref")
    }

    class UpdateRef(val refName: String, val refLocation: String, val enforceNewRefName: Boolean): GitCommand() {
        override val command: List<String>; get() {
            val commands = mutableListOf("git", "update-ref", refName, refLocation)
            if (enforceNewRefName) {
                commands.add("") // Adding the empty string enforces that we're creating a branch not moving one
            }

            return commands
        }
    }

    class ForEachBranchRef : GitCommand() {
        companion object {
            const val deliminator = ':'
            private const val format = "%(refname)$deliminator%(HEAD)$deliminator%(upstream)$deliminator%(objectname)$deliminator%(objecttype)"
        }

        override val command: List<String> = listOf("git", "for-each-ref", "--format=$format", "refs/heads", "refs/remotes")
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
        override val command: List<String> get() = listOf("git", "add", file.canonicalPath)
    }

    class UnStageFile(val file: File): GitCommand() {
        override val command: List<String> get() = listOf("git", "reset", file.canonicalPath)
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
