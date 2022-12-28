package service

import entity.*
import view.Refreshable
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

/**
 * This class is used to test the Service class [RootService]
 * **/
class RootServiceTest {

    /**
     * Creates a new [RootService] object and tests it
     * **/
    @Test
    fun testConstructor() {
        val history = History()
        val playerInfo = PlayerInfo("TestName",PlayerType.AI_HARD , Color.BLUE, isNetworkPlayer = true)
        val stationTiles = listOf<StationTile>()
        val activePlayer = Player(playerInfo, stationTiles)
        val drawPile = mutableListOf<GameTile>()
        val board = arrayOf(arrayOf<Tile?>())
        val players = listOf<Player>()
        val currentState = State(drawPile, activePlayer, board, players)
        var cableCar = CableCar(false, 1, false, GameMode.HOTSEAT, history, currentState)
        val rootService = RootService()
        var playerActionService = PlayerActionService(rootService)
        var cableCarService = CableCarService(rootService)
        var setupService = SetupService(rootService)
        var ioService = IOService(rootService)
        val aiService = AIService(rootService)
        var networkService = NetworkService(rootService)

        assertEquals(rootService, rootService)
        assertNotNull(rootService)
    }

}