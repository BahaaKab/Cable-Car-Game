package entity

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue

/**
 * This class is used to test the Entity class [CableCar]
 */

class CableCarTest {
    private var connections = listOf(1,2,3,4,5,6,7,8)
    private var stationTile:StationTile = StationTile(listOf(1,2))
    private var stationTiles = listOf(stationTile)
    private var gameTile:GameTile = GameTile(1,connections)
    private var player1 : Player = Player(PlayerType.HUMAN,Color.BLUE,"TestName1", stationTiles)
    private var player2 : Player = Player(PlayerType.HUMAN,Color.GREEN,"TestName2", stationTiles)
    private var tile:Tile? = Tile(listOf(1,2,3,4), listOf(1,4), isEmpty = false, isEndTile = false)
    private var tile1:Tile? = Tile(listOf(2,3,4,5), listOf(2,5), isEmpty = false, isEndTile = false)
    private var board = arrayOf(arrayOf(tile,tile1))
    private var state:State = State(mutableListOf(gameTile),player1,board,listOf(player1,player2))
    private var history:History = History()
    private var cableCar: CableCar = CableCar(true, 5 , false, GameMode.HOTSEAT, history, state)
    @Test
    fun testConstructor() {
        assertTrue(cableCar.allowTileRotation)
        assertEquals(state,cableCar.currentState)
        assertEquals(GameMode.HOTSEAT,cableCar.gameMode)
        //A new value for AISpeed is assigned and checked if it has been assigned properly.
        cableCar.AISpeed = 3
        assertEquals(3,cableCar.AISpeed)
        //A new value for currentState is assigned and checked if the currentState has changed.
        cableCar.currentState = State(mutableListOf(gameTile),player2,board,listOf(player1,player2))
        assertNotEquals(state,cableCar.currentState)
    }
}