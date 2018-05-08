package org.littlegit.core.reader

import org.littlegit.core.commandrunner.CommitHash
import org.littlegit.core.model.RawCommit
import org.littlegit.core.util.BiMap
import org.littlegit.core.util.Grid
import org.littlegit.core.util.GridIndex

sealed class GridEntry {
    data class Commit(val rawCommit: RawCommit): GridEntry()
    class Left: GridEntry()
    class Right: GridEntry()
    class Horizontal: GridEntry()
    class Vertical: GridEntry()
}

class GridGraph(graph: GitGraph) {

    val grid = Grid<GridEntry>()

    init {
        var nextFreeRow = 0
        val reservedColumns = BiMap<Int, CommitHash>()
        val commitPositions = HashMap<RawCommit, GridIndex>()

        graph.forEach { node ->

            val column = getColumn(reservedColumns, node.commit)

            val row = nextFreeRow
            nextFreeRow += 2

            val commitPos = GridIndex(row, column)
            commitPositions[node.commit] = commitPos
            grid.set(commitPos, GridEntry.Commit(node.commit))

            node.commit.parentHashes.forEach { parentHash ->
                if (!reservedColumns.containsVal(parentHash)) {
                    reservedColumns.put(getNextFreeColumn(reservedColumns), parentHash)
                }
            }

            node.children.forEach { weakNode ->
                weakNode.get()?.let { node ->
                    commitPositions[node.commit]?.let { childPos ->

                        var currentPathIndex: GridIndex = commitPos

                        while (!currentPathIndex.isAdjacentTo(commitPos)) {
                            if (currentPathIndex.column < childPos.column) {
                                val northEast = commitPos.northEast
                                val east = commitPos.east

                                if (grid.get(northEast) == null) {
                                    grid.set(northEast, GridEntry.Right())
                                    currentPathIndex = northEast
                                } else if (grid.get(east) == null) {
                                    grid.set(east, GridEntry.Horizontal())
                                    currentPathIndex = east
                                }
                            } else if (currentPathIndex.column > childPos.column) {
                                val northWest = commitPos.northWest
                                val west = commitPos.west

                                if (grid.get(northWest) == null) {
                                    grid.set(northWest, GridEntry.Left())
                                    currentPathIndex = northWest
                                } else if (grid.get(west) == null) {
                                    grid.set(west, GridEntry.Horizontal())
                                    currentPathIndex = west
                                }
                            } else {
                                val north = childPos.north
                                if (grid.get(north) == null) {
                                    grid.set(north, GridEntry.Vertical())
                                    currentPathIndex = north
                                }
                            }
                        }
                    }
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
            nextCol += 3
        }

        return nextCol
    }

}