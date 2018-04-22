package org.littlegit.core.shell

import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.run
import kotlinx.coroutines.experimental.runBlocking
import java.io.File
import java.io.InputStreamReader
import java.io.BufferedReader

typealias ShellRunnerCallback = (result: ShellResult) -> Unit

sealed class ShellResult {
    data class Success(val lines: List<String>): ShellResult()
    data class Error(val lines: List<String>): ShellResult()
}

object ShellRunner {

    fun runCommand(basePath: String, commands: List<String>, callback: ShellRunnerCallback) {

        val runCommand = async { run(basePath, commands) }

        runBlocking {
            callback(runCommand.await())
        }
    }

    private fun run(basePath: String, command: List<String>): ShellResult {
        val pb = ProcessBuilder(command)

        val workingFolder = File(basePath)
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