package org.littlegit.core.unit.resultparser
import junit.framework.TestCase.*
import org.junit.Test
import org.littlegit.core.util.GridIndex

class GridIndexTests {

    private val gridIndex = GridIndex(5,5)

    @Test fun testNorth() = assertEquals(gridIndex.north, GridIndex(4, 5))
    @Test fun testEast() = assertEquals(gridIndex.east, GridIndex(5, 6))
    @Test fun testWest() = assertEquals(gridIndex.west, GridIndex(5, 4))
    @Test fun testSouth() = assertEquals(gridIndex.south, GridIndex(6, 5))
    @Test fun testNorthEast() = assertEquals(gridIndex.northEast, GridIndex(4, 6))
    @Test fun testNorthWest() = assertEquals(gridIndex.northWest, GridIndex(4, 4))
    @Test fun testSouthEast() = assertEquals(gridIndex.southEast, GridIndex(6, 6))
    @Test fun testSouthWest() = assertEquals(gridIndex.southWest, GridIndex(6, 4))

    @Test fun adjacencyTest() {

        assertTrue(gridIndex.isAdjacentTo(GridIndex(4, 5)))
        assertTrue(gridIndex.isAdjacentTo(GridIndex(5, 6)))
        assertTrue(gridIndex.isAdjacentTo(GridIndex(5, 4)))
        assertTrue(gridIndex.isAdjacentTo(GridIndex(6, 5)))
        assertTrue(gridIndex.isAdjacentTo(GridIndex(4, 6)))
        assertTrue(gridIndex.isAdjacentTo(GridIndex(6, 6)))
        assertTrue(gridIndex.isAdjacentTo(GridIndex(6, 4)))

        assertFalse(gridIndex.isAdjacentTo(GridIndex(0, 0)))
        assertFalse(gridIndex.isAdjacentTo(GridIndex(3, 3)))
    }
}