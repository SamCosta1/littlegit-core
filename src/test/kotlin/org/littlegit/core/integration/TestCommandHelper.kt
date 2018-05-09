package org.littlegit.core.integration

import java.io.File
import java.io.InputStreamReader
import java.io.BufferedReader



class TestCommandHelper(private val file: File) {

    fun init(): TestCommandHelper {
        execute("git init")
        return this
    }

    fun commit(message: String = "test message"): TestCommandHelper {
        execute("git commit -m \"$message\"")
        return this
    }

    fun addAll(): TestCommandHelper {
        execute("git add *")
        return this
    }

    fun addRemote(remote: String) {
        execute("git remote add $remote www.remote.com")
    }

    fun getLastCommitMessage(): String {
        return execute("git log -1 --pretty=%B")
    }

    private fun execute(command: String): String {

        val output = StringBuffer()

        val p: Process
        try {
            p = Runtime.getRuntime().exec(command, emptyArray(), file)
            p.waitFor()
            val reader = BufferedReader(InputStreamReader(p.inputStream))

            var line: String? = ""
            do {
                line = reader.readLine()

                if (line != null && line.isNotBlank()) {
                    output.append(line)
                }

            } while (line != null)

        } catch (e: Exception) {
            e.printStackTrace()
        }

        return output.toString().trim()
    }
}