package org.littlegit.core.reader

import org.littlegit.core.commandrunner.CommitHash
import org.littlegit.core.model.RawCommit
import org.littlegit.core.util.BiMap
import org.littlegit.core.util.Grid

sealed class GridEntry {
    data class Commit(val rawCommit: RawCommit): GridEntry()
    class Left: GridEntry()
    class Right: GridEntry()
    class Horizontal: GridEntry()
}

data class GridIndex(val row: Int, val column: Int)

class GridGraph(commits: List<RawCommit>) {

    val grid = Grid<GridEntry>()

    init {
        var nextFreeRow = 0
        val reservedColumns = BiMap<Int, CommitHash>()
        val commitPositions = HashMap<RawCommit, GridIndex>()

        commits.forEach { commit ->
            val column = getColumn(reservedColumns, commit)

            val row = nextFreeRow++

            commitPositions.put(commit)
            grid.set(row, column, GridEntry.Commit(commit))

            commit.parentHashes.forEach { parentHash ->
                if (!reservedColumns.containsVal(parentHash)) {
                    reservedColumns.put(getNextFreeColumn(reservedColumns), parentHash)
                }
            }
        }
    }

    private fun getColumn(reservedColumns: BiMap<Int, CommitHash>, commit: RawCommit): Int {
        val reservedOrNull = reservedColumns.getKey(commit.hash)
        if (reservedOrNull != null) {
            reservedColumns.remove(reservedOrNull)
            return reservedOrNull
        }

        return getNextFreeColumn(reservedColumns)
    }

    private fun getNextFreeColumn(reservedColumns: BiMap<Int, CommitHash>): Int {
        var nextCol = 0
        while (reservedColumns.contains(nextCol)) {
            nextCol++
        }

        return nextCol
    }

}