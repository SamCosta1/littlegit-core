package org.littlegit.core.util

class GridIndex(val row: Int, val column: Int) {

    val north: GridIndex; get() = GridIndex(row - 1, column)
    val south: GridIndex; get() = GridIndex(row + 1, column)
    val east: GridIndex; get() = GridIndex(row, column + 1)
    val west: GridIndex; get() = GridIndex(row, column - 1)
    val northEast: GridIndex; get() = GridIndex(row - 1, column + 1)
    val northWest: GridIndex; get() = GridIndex(row - 1, column - 1)
    val southEast: GridIndex; get() = GridIndex(row + 1, column + 1)
    val southWest: GridIndex; get() = GridIndex(row + 1, column - 1)

    val isPositive: Boolean; get() = row >= 0 && column >= 0

    fun isAdjacentTo(other: GridIndex) = listOf(north, south, east, west, northEast, northWest, southEast, southWest).contains(other)

    override fun equals(other: Any?): Boolean {
        return if (other is GridIndex) {
            other.row == row && other.column == column
        } else {
            super.equals(other)
        }
    }

    override fun hashCode(): Int {
        var result = row
        result = 31 * result + column
        return result
    }

    override fun toString(): String {
        return "{ row: $row, column: $column }"
    }
}

class Grid<T>: Iterable<MutableList<T?>> {

    private val grid: MutableList<MutableList<T?>> = ArrayList()

    fun set(row: Int, column: Int, entry: T) {
        addRowsIfNeeded(row)
        addColumnsIfNeeded(row, column)

        grid[row][column] = entry
    }

    fun set(index: GridIndex, entry: T) {
        set(index.row, index.column, entry)
    }

    fun row(index: Int): List<T?> {
        if (index > grid.size) {
            return emptyList()
        }

        return grid[index].toList()

    }

    private fun addColumnsIfNeeded(row: Int, column: Int) {
        if (column >= grid[row].size) {
            for (index: Int in grid[row].size..column) {
                grid[row].add(null)
            }
        }
    }

    private fun addRowsIfNeeded(row: Int) {
        if (row >= grid.size) {
            for (index: Int in grid.size..row+1) {

                grid.add(ArrayList())
            }
        }
    }

    fun get(index: GridIndex): T? {
        return get(index.row, index.column)
    }

    fun get(row: Int, column: Int): T? {
        if (row < 0 || row >= grid.size) {
            return null
        }

        if (column < 0 || column >= grid[row].size) {
            return null
        }

        return grid[row][column]
    }

    override fun iterator(): Iterator<MutableList<T?>> {
        return grid.iterator()
    }
}