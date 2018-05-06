package org.littlegit.core.unit.resultparser

import org.junit.Rule
import org.junit.Test
import org.littlegit.core.helper.LocalResourceFile
import org.littlegit.core.parser.LogParser
import org.littlegit.core.reader.AsciiGraph

class PrintAsciiGraph {
    @Rule val largeRepo = LocalResourceFile("largeRepo.txt")

    @Test fun printRepo() {
        val commits = LogParser.parse(largeRepo.content)
        println(AsciiGraph.getAsciiGraph(commits))
    }
}