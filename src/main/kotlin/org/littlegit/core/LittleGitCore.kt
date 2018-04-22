package org.littlegit.core

import org.littlegit.core.modifier.RepoModifier
import org.littlegit.core.reader.RepoReader
import org.littlegit.core.shell.ShellRunner

class LittleGitCore(repoDirectoryPath: String) {

    init {
        ShellRunner.initializeRootDirectory(repoDirectoryPath)
    }

    var repoReader = RepoReader()
    var repoModifier = RepoModifier()
}