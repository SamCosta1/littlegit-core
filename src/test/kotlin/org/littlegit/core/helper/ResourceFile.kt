package org.littlegit.core.helper

import java.io.BufferedReader
import java.io.File
import java.io.FileOutputStream
import java.io.FileReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.nio.charset.Charset

import org.junit.rules.ExternalResource

open class ResourceFile(private var res: String) : ExternalResource() {
    private var file: File? = null
    private var inputStream: InputStream? = null

    val content: List<String>
        @Throws(IOException::class)
        get() {
            val lineList = mutableListOf<String>()
            inputStream?.bufferedReader()?.useLines { lines -> lines.forEach { lineList.add(it)} }
            return lineList
        }

    @Throws(Throwable::class)
    override fun before() {
        super.before()
        inputStream = File(res).inputStream()
    }

    override fun after() {
        try {
            inputStream?.close()
        } catch (e: IOException) {
            // ignore
        }

        if (file != null) {
            file!!.delete()
        }
        super.after()
    }
}