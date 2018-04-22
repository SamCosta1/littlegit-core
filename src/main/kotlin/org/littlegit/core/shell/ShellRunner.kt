package org.littlegit.core.shell

import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.runBlocking
import java.io.File
import java.io.InputStreamReader
import java.io.BufferedReader

interface ShellRunnerCallback {
    fun onExecFinished(result: ShellResult)
}

sealed class ShellResult {
    data class Success(val lines: List<String>): ShellResult()
    data class Error(val lines: List<String>): ShellResult()
}

object ShellRunner {

    private var rootDirectoryPath: String? = null

    fun initializeRootDirectory(path: String) {
        if (path.isNotBlank()) {
            rootDirectoryPath = path
        }
    }

    fun runCommand(repoPath: String? = null, vararg commands: String, callback: ShellRunnerCallback) {
        if (repoPath == null && ShellRunner.rootDirectoryPath == null) {
            throw RuntimeException("Repo path must be supplied or ShellRunner initialised")
        }

        val runCommand = async { runCommand(*commands) }

        runBlocking {
            callback.onExecFinished(runCommand.await())
        }
    }

    private fun runCommand(vararg command: String): ShellResult {
        val pb = ProcessBuilder(*command)

        val workingFolder = File(rootDirectoryPath)
        pb.directory(workingFolder)

        val proc = pb.start()

        val stdInput = BufferedReader(InputStreamReader(proc.inputStream))
        val stdError = BufferedReader(InputStreamReader(proc.errorStream))

        val lines = ArrayList<String>()
        var line : String?

        do {
            line = stdInput.readLine()

            if (line != null && line.isNotBlank()) {
                lines.add(line)
            }
        } while (line != null)

        val errLines = ArrayList<String>()
        do {
            line = stdError.readLine()
            if (line != null && line.isNotBlank()) {
                errLines.add(line)
            }
        } while (line != null)

        return if (errLines.isEmpty()) ShellResult.Success(lines) else ShellResult.Error(errLines)
    }
}