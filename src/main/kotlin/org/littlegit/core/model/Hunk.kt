package org.littlegit.core.model

data class Hunk(val fromStartLine: Int,
                val numFromLines: Int,
                val toStartLine: Int,
                val numToLines: Int,
                val hunkHeader: String,
                val lines: List<DiffLine>)