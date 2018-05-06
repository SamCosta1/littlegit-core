package org.littlegit.core.unit.resultparser

import junit.framework.TestCase.assertNull
import junit.framework.TestCase.assertTrue
import org.junit.Test
import org.littlegit.core.util.Grid

class GridTests {

    @Test fun getNonExistingTest() {
        val grid = Grid<String>()

        assertNull("Value is null", grid.get(400,400))
    }

    @Test fun setNonExistingTest() {
        val entry = "Test"
        val row = 2340
        val col = 100
        val grid = Grid<String>()

        grid.set(row, col, entry)
        assertTrue("Entry is as expected", grid.get(row, col) == entry)
    }

    @Test fun setGetOneTooMuchTest() {
        val entry = "Test"
        val grid = Grid<String>()

        for (rowIndex: Int in 0..5) {
            for (colIndex: Int in 0..5) {
                grid.set(rowIndex, colIndex, entry)
                assertTrue("Entry is as expected", grid.get(rowIndex, colIndex) == entry)
            }
        }

        assertNull("Value is null", grid.get(6,6))
    }
}