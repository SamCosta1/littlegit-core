package org.littlegit.core.reader

import org.littlegit.core.model.RawCommit

object AsciiGraph {


    fun getAsciiGraph(graph: GitGraph, newlineDelimiter: String = System.getProperty("line.separator")): String {
        val grid = GridGraph(graph).grid

        val builder = StringBuilder()
        grid.forEach { row ->
            row.forEach { column ->

                var commit: RawCommit? = null
                when (column) {
                    is GridEntry.Commit -> {
                        commit = column.rawCommit
                        builder.append('*')
                    }
                    is GridEntry.Horizontal -> builder.append('-')
                    is GridEntry.Left -> builder.append('\\')
                    is GridEntry.Right -> builder.append('/')
                    is GridEntry.Vertical -> builder.append('|')
                else -> builder.append(" ")
                }

                commit?.let { builder.append("          ${commit.commitSubject}                ${commit.hash} |  ${commit.parentHashes}")}
            }


            builder.append(newlineDelimiter)
        }

        return builder.toString()
    }
}