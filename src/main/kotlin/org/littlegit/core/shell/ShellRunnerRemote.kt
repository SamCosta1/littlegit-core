package org.littlegit.core.shell

import org.littlegit.core.util.joinWithSpace
import java.io.BufferedReader
import java.io.InputStreamReader
import java.nio.file.Path

class ShellRunnerRemote(private val user: String, private val host: String, private val repoPath: Path) : ShellRunner {

    override fun runCommand(command: List<String>): ShellResult {
        val gitCommand = "cd ${repoPath.toFile().canonicalPath} && ${command.joinWithSpace()}"
        val sshCommand = mutableListOf("ssh", "-oStrictHostKeyChecking=no", "$user@$host", gitCommand)
        val pb = ProcessBuilder(sshCommand.toList())

        val proc = pb.start()

        val stdInput = BufferedReader(InputStreamReader(proc.inputStream))
        val stdError = BufferedReader(InputStreamReader(proc.errorStream))

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