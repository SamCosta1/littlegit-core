package org.littlegit.core.helper

class LocalResourceFile(localPath: String): ResourceFile("${System.getProperty("user.dir")}/src/test/kotlin/testFiles/$localPath")