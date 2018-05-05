package org.littlegit.core

import org.littlegit.core.modifier.ConfigModifier
import org.littlegit.core.modifier.RepoModifier
import org.littlegit.core.reader.RepoReader
import org.littlegit.core.commandrunner.GitCommandRunner

class LittleGitCore(repoDirectoryPath: String) {

    private val commandRunner = GitCommandRunner().initializeRepoDirectory(repoDirectoryPath)

    val repoReader = RepoReader(commandRunner)
    val repoModifier = RepoModifier(commandRunner)
    val configModifier = ConfigModifier(commandRunner)
}