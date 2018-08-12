package org.littlegit.core.modifier
import org.littlegit.core.LittleGitCommandResult
import org.littlegit.core.commandrunner.CommitHash
import org.littlegit.core.commandrunner.GitCommand
import org.littlegit.core.commandrunner.GitCommandRunner
import org.littlegit.core.commandrunner.GitResult
import org.littlegit.core.model.*
import org.littlegit.core.reader.RepoReader
import org.littlegit.core.util.FileUtils
import java.io.File


class RepoModifier(private val commandRunner: GitCommandRunner, private val repoReader: RepoReader) {

    fun initializeRepo(bare: Boolean = false, name: String? = null): LittleGitCommandResult<Unit>
            = commandRunner.runCommand(command = GitCommand.InitializeRepo(bare, name))

    fun commit(message: String): LittleGitCommandResult<Unit> = commit(listOf(message))


    fun commit(message: List<String>): LittleGitCommandResult<Unit> {

        val tempFile = FileUtils.writeToTempFile("littlegit-commit-message", System.nanoTime().toString(), content = message)

        var result: LittleGitCommandResult<Unit>? = null
        tempFile?.let {
            result = commandRunner.runCommand(command = GitCommand.Commit(it))

        }

        tempFile?.delete()

        return result ?: LittleGitCommandResult(null, GitResult.Error(GitError.Unknown(listOf())))
    }

    fun push(remote: String? = null, branch: String? = null, setUpstream: Boolean = false): LittleGitCommandResult<Unit> {
        if (setUpstream && ( branch.isNullOrBlank() || remote.isNullOrBlank() )) {
            throw IllegalArgumentException("Remote and branch must be non empty when setting upstream")
        }

        val command = if (setUpstream) {
            GitCommand.PushSetUpstream(remote!!, branch!!)
        } else {
            GitCommand.Push(remote, branch)
        }

        return commandRunner.runCommand(command = command)
    }

    fun addRemote(remoteName: String, remoteUrl: String): LittleGitCommandResult<Unit>
            = commandRunner.runCommand(command = GitCommand.AddRemote(remoteName, remoteUrl))

    fun stageFile(file: File): LittleGitCommandResult<Unit> = commandRunner.runCommand(command = GitCommand.StageFile(file))
    fun unStageFile(file: File): LittleGitCommandResult<Unit>  = commandRunner.runCommand(command = GitCommand.UnStageFile(file))

    fun stageHunk(hunk: Hunk, fileDiff: FileDiff.ChangedFile): LittleGitCommandResult<Unit> {
        val patch = hunk.generatePatch(fileDiff)
        return applyPatch(patch)
    }

    fun unStageHunk(hunk: Hunk, fileDiff: FileDiff.ChangedFile): LittleGitCommandResult<Unit> {
        val patch = hunk.generateInversePatch(fileDiff)
        return applyPatch(patch)
    }

    fun applyPatch(patch: Patch): LittleGitCommandResult<Unit> {
        val tempFile = FileUtils.writeToTempFile("littlegit-patch", System.nanoTime().toString(), patch)

        var result: LittleGitCommandResult<Unit>? = null
        tempFile?.let {
            result = commandRunner.runCommand(command = GitCommand.ApplyPatch(it))
        }

        tempFile?.delete()
        return result ?: LittleGitCommandResult(null, GitResult.Error(GitError.Unknown(listOf())))
    }

    fun createBranch(branchName: String): LittleGitCommandResult<Unit> {
        return this.createBranch(branchName, "HEAD")
    }

    fun createBranch(branchName: String, commit: RawCommit): LittleGitCommandResult<Unit> {
        return this.createBranch(branchName, commit.hash)
    }

    private fun createBranch(branchName: String, location: String): LittleGitCommandResult<Unit> {

        val refName = if (branchName.startsWith("refs/heads/")) {
            branchName
        } else {
            "refs/heads/${branchName.removePrefix("/")}"
        }

        return commandRunner.runCommand(command = GitCommand.UpdateRef(refName, location, true))
    }

    /**
     * @param branch The branch to checkout
     * @param moveUnCommittedChanges When set to true, will attempt to move any unstaged changes to the new branch by stashing them
     *                              When set to false, will error if git errors from being unable to merge the working tree
     *
     */
    fun checkoutBranch(branch: LocalBranch, moveUnCommittedChanges: Boolean = true): LittleGitCommandResult<Unit> {

        // Check the branch exists first
        val upToDateBranch = repoReader.getBranch(branch).data ?: return LittleGitCommandResult.buildError(GitError.BranchNotFound(emptyList()))

        var stashCommitHash: CommitHash? = null

        if (moveUnCommittedChanges) {
            stashCommitHash = commandRunner.runCommand(GitCommand.CreateStash(), { success: GitResult.Success -> success.lines.firstOrNull() }).data
            commandRunner.runCommand<Unit>(GitCommand.Reset(ResetType.Hard))
        }

        // First update the working directory of the new branch
        val updateWDResult = commandRunner.runCommand<Unit>(command = GitCommand.ReadTreeHead(branch = branch))

        if (updateWDResult.result is GitResult.Error) {
            return updateWDResult
        }

        // Apply the changes from the stash back onto the working directory, if this command fails we'll still update the HEAD
        // Because otherwise HEAD and the working tree will be out of sync
        if (moveUnCommittedChanges && !stashCommitHash.isNullOrBlank()) {
            commandRunner.runCommand<Unit>(GitCommand.ApplyStashCommit(stashCommitHash!!))
        }

        // Now update the HEAD to point at new branch
        return commandRunner.runCommand(command = GitCommand.SymbolicRef(branch = upToDateBranch))
    }

}
