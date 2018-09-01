package org.littlegit.core.util

import org.littlegit.core.LittleGitCommandResult
import java.io.File

object FileUtils {
    fun writeToTempFile(fileNamePrefix: String, fileNameSuffix: String, content: List<String>): File? {
        var result: LittleGitCommandResult<Unit>? = null
        var tempFile: File? = null
        return try {
            tempFile = File.createTempFile(fileNamePrefix, fileNameSuffix)
            ListUtils.writeToFile(content, tempFile)

            tempFile
        } catch (e: Exception) {
            null
        }
    }
}