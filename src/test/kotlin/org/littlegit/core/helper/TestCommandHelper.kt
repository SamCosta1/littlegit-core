package org.littlegit.core.helper

import java.io.File
import java.io.InputStreamReader
import java.io.BufferedReader
import java.nio.charset.Charset
import java.nio.file.Files
import java.nio.file.Paths


class TestCommandHelper(private val file: File) {

    companion object {
        const val DEFAULT_EMAIL =  "frodo.baggins@shire.com"
        const val DEFAULT_NAME = "Frodo Baggins"
    }

    fun initConfig(name: String = DEFAULT_NAME, email: String = DEFAULT_EMAIL): TestCommandHelper {
        execute("git config user.name $name")
        execute("git config user.email $email")

        return this;
    }

    fun init(): TestCommandHelper {
        execute("git init")
        return this
    }

    fun commit(message: String = "test message"): TestCommandHelper {
        execute("git commit -m $message")
        return this
    }

    fun addAll(): TestCommandHelper {
        execute("git add .")
        return this
    }

    fun addRemote(remote: String, url: String = "www.remote.com"): TestCommandHelper {
        execute("git remote add $remote $url")
        return this
    }

    fun getLastCommitMessage(): String {
        return execute("git log -1 --pretty=%B")
    }

    fun getLastCommitHash(): String {
        return execute("git log -1 --pretty=%H")
    }

    fun getLastCommitTimeStamp(): String {
        return execute("git log -1 --date=iso --pretty=%ct")
    }

    fun run(command: String): String {
        return execute(command)
    }

    fun writeToFile(file: String, content: String): TestCommandHelper {

        val path = Paths.get("${this.file.absolutePath}/$file")
        Files.write(path, listOf(content), Charset.forName("UTF-8"))
        return this
    }

    fun execute(command: String): String {
        val output = StringBuffer()

        val p: Process
        try {
            p = Runtime.getRuntime().exec(command, emptyArray(), file)
            p.waitFor()
            val reader = BufferedReader(InputStreamReader(p.inputStream))
            val error = BufferedReader(InputStreamReader(p.errorStream))

            var line: String? = ""
            do {
                line = error.readLine()
                if (line != null && line.isNotBlank()) {
                    println(line)
                }
            } while (line != null)

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

    fun branchAndCheckout(branch: String): TestCommandHelper {
        execute("git checkout -b $branch")
        return this
    }
}