package org.littlegit.core.shell

import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader
import java.nio.charset.Charset

class ShellRunnerLocal(private val basePath: String): ShellRunner {

    override fun runCommand(command: List<String>): ShellResult {
        val pb = ProcessBuilder(command)

        val workingFolder = File(basePath)
        pb.directory(workingFolder)

        val proc = pb.start()

        val stdInput = BufferedReader(InputStreamReader(proc.inputStream, Charset.forName("UTF-8")))
        val stdError = BufferedReader(InputStreamReader(proc.errorStream, Charset.forName("UTF-8")))

        val lines = ArrayList<String>()
        var line : String?

        do {
            line = stdInput.readLine()

            if (line != null) {
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