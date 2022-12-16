package entity

import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * Tests for the Tile-Entities
 */
class TileTest {

    /**
     * Tests the CornerTile-Constructor
     */
    @Test
    fun testCornerTileConstructor() {
        val cornerTile : CornerTile = CornerTile()
        assertEquals(cornerTile.isEndTile, false)
        assertEquals(cornerTile.isEmpty, true)
        assertEquals(cornerTile.connections.size, 0)
        assertEquals(cornerTile.connectors.size, 0)
    }

    /**
     * Tests the StationTile-Constructor
     */
    @Test
    fun testStationTileConstructor() {
        val connectors : List<Int> = listOf(0,1)
        val stationTile : StationTile = StationTile(connectors)
        assertEquals(stationTile.startPosition, 1)
        assertEquals(stationTile.connectors, connectors)
        assertEquals(stationTile.isEndTile, true)
        assertEquals(stationTile.isEmpty, false)
        assertEquals(stationTile.connections.size, 0)
    }

    /**
     * Tests the PowerStationTileConstructor
     */
    @Test
    fun testPowerStationTileConstructor() {
        val connectors : List<Int> = listOf(2,3,5,4);
        val powerStationTile : PowerStationTile = PowerStationTile(connectors)
        assertEquals(powerStationTile.isEndTile, true)
        assertEquals(powerStationTile.isEmpty, false)
        assertEquals(powerStationTile.connectors, connectors)
        assertEquals(powerStationTile.connections.size, 0)
        // Test for method that updates the points
        assertEquals(powerStationTile.updatePoints(4), 8)
    }

    /**
     * Tests the GameTileConstructor
     */
    @Test
    fun testGameTileConstructor() {
        val connectors : List<Int> = listOf(0,1,2,3,4,5,6,7)
        val connections : List<Int> = listOf(2,6,0,4,3,7,1,5)
        val gameTileID : Int = 42
        val gameTile : GameTile = GameTile(gameTileID, connections)
        assertEquals(gameTile.connectors, connectors)
        assertEquals(gameTile.connections, connections)
        assertEquals(gameTile.id, gameTileID)
        // Test for method that updates the points
        assertEquals(gameTile.updatePoints(4),5)
    }
}