package entity

import org.junit.jupiter.api.assertThrows
import kotlin.IllegalArgumentException
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
        assertEquals(false, cornerTile.isEndTile)
        assertEquals(true, cornerTile.isEmpty)
        assertEquals(0, cornerTile.connections.size)
        assertEquals(0, cornerTile.connectors.size)
    }

    /**
     * Tests the StationTile-Constructor
     */
    @Test
    fun testStationTileConstructor() {
        val connectors : List<Int> = listOf(0,1)
        val stationTile : StationTile = StationTile(connectors)
        assertEquals(1, stationTile.startPosition)
        assertEquals(connectors, stationTile.connectors)
        assertEquals(true, stationTile.isEndTile)
        assertEquals(false, stationTile.isEmpty)
        assertEquals(0, stationTile.connections.size)
    }

    /**
     * Tests the PowerStationTileConstructor
     */
    @Test
    fun testPowerStationTileConstructor() {
        val connectors : List<Int> = listOf(2,3,5,4);
        val powerStationTile : PowerStationTile = PowerStationTile(connectors)
        assertEquals(true, powerStationTile.isEndTile)
        assertEquals(false, powerStationTile.isEmpty)
        assertEquals(connectors, powerStationTile.connectors)
        assertEquals(0, powerStationTile.connections.size)
        // Test for methode that updates the points
        assertEquals(8, powerStationTile.updatePoints(4))
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
        assertEquals(connectors, gameTile.connectors)
        assertEquals(connections, gameTile.connections)
        assertEquals(gameTileID, gameTile.id)
        // Test for method that updates the points
        assertEquals(5, gameTile.updatePoints(4))
    }

    /**
     * Tests an IllegalArgumentException for constructor of StationTile
     */
    @Test
    fun testStationTileFail(){
        val connectors : List<Int> = listOf(1)
        assertThrows<IllegalArgumentException>{
            StationTile(connectors)
        }
    }

    /**
     * Tests an IllegalArgumentException for constructor of PowerStationTile
     */
    @Test
    fun testPowerStationTileFail(){
        val connectors : List<Int> = listOf(0,1,2)
        assertThrows<IllegalArgumentException>{
            PowerStationTile(connectors)
        }
    }

    /**
     * Tests an IllegalArgumentException for constructor of PowerStationTile
     */
    @Test
    fun testGameTileFail(){
        val connections : List<Int> = listOf(1,0)
        val gameTileId : Int = 42
        assertThrows<IllegalArgumentException>{
            GameTile(gameTileId, connections)
        }
    }
}