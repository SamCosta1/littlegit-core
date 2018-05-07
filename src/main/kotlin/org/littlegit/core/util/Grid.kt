package org.littlegit.core.util

class Grid<T>: Iterable<MutableList<T?>> {

    private val grid: MutableList<MutableList<T?>> = ArrayList()

    fun set(row: Int, column: Int, entry: T) {
        addRowsIfNeeded(row)
        addColumnsIfNeeded(row, column)

        grid[row][column] = entry
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

    fun get(row: Int, column: Int): T? {
        if (row >= grid.size) {
            return null
        }

        if (column >= grid[row].size) {
            return null
        }

        return grid[row][column]
    }

    override fun iterator(): Iterator<MutableList<T?>> {
        return grid.iterator()
    }
}