package org.littlegit.core

import org.littlegit.core.modifier.RepoModifier
import org.littlegit.core.reader.RepoReader
import org.littlegit.core.shell.GitCommandRunner

class LittleGitCore(repoDirectoryPath: String) {

    private val commandRunner = GitCommandRunner().initializeRepoDirectory(repoDirectoryPath)

    var repoReader = RepoReader(commandRunner)
    var repoModifier = RepoModifier(commandRunner)

}