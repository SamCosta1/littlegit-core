package org.littlegit.core.model

enum class DiffLineType {
    Addition,
    Deletion,
    Unchanged
}

// When type is unchanged, fromLineNum = toLineNum
data class DiffLine(val type: DiffLineType,
                    val fromLineNum: Int? = null,
                    val toLineNum: Int? = null,
                    val line: String)