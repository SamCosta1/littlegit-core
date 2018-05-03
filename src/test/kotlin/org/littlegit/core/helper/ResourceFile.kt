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

class ResourceFile(private var res: String) : ExternalResource() {
    private var file: File? = null
    private var inputStream: InputStream? = null

    val content: List<String>
        @Throws(IOException::class)
        get() = getContent("utf-8")

    @Throws(IOException::class)
    fun getFile(): File? {
        if (file == null) {
            createFile()
        }
        return file
    }

    private fun createInputStream(): InputStream {
        return javaClass.getResourceAsStream(res)
    }

    @Throws(IOException::class)
    fun getContent(charSet: String): List<String> {
        val streamReader = InputStreamReader(createInputStream(), Charset.forName(charSet))

        return streamReader.readLines()
    }

    @Throws(Throwable::class)
    override fun before() {
        super.before()
        inputStream = javaClass.getResourceAsStream(res)
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

    @Throws(IOException::class)
    private fun createFile() {
        file = File(".", res)
        val resourceStream = javaClass.getResourceAsStream(res)
        resourceStream.use { stream ->

            file?.createNewFile()
            var ostream: FileOutputStream? = null
            try {
                ostream = FileOutputStream(file!!)
                val buffer = ByteArray(4096)
                while (true) {
                    val len = stream.read(buffer)
                    if (len < 0) {
                        break
                    }
                    ostream.write(buffer, 0, len)
                }
            } finally {
                if (ostream != null) {
                    ostream.close()
                }
            }
        }
    }

}