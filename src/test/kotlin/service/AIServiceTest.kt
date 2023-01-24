package service

import entity.*
import org.junit.jupiter.api.assertThrows
import kotlin.test.*

class AIServiceTest {
    @Test
    fun testplaceable() {
        val rootService = RootService()
        val aiActivePlayer = Player(PlayerType.AI_EASY, Color.BLUE, "testy", listOf(), false)
        val testBoard: Array<Array<Tile?>> = Array(10) {
            Array(10) { null }
        }
        for (x in 0..9) {
            testBoard[x][0] = StationTile(listOf(0, 1))
            testBoard[x][9] = StationTile(listOf(0, 1))
        }
        for (y in 0..9) {
            testBoard[0][y] = StationTile(listOf(0, 1))
            testBoard[9][y] = StationTile(listOf(0, 1))
        }
        testBoard[4][4] = PowerStationTile(listOf(0, 1, 2, 3))
        testBoard[4][5] = PowerStationTile(listOf(0, 1, 2, 3))
        testBoard[5][4] = PowerStationTile(listOf(0, 1, 2, 3))
        testBoard[5][5] = PowerStationTile(listOf(0, 1, 2, 3))

        rootService.cableCar = CableCar(
            true, 10, true, GameMode.HOTSEAT, History(),
            State(rootService.ioService.getTilesFromCSV().toMutableList(), aiActivePlayer, testBoard, listOf())
        )
        rootService.cableCar.currentState.drawPile.add(0, GameTile(61, listOf(1, 0, 3, 2, 5, 4, 7, 6)))
        rootService.cableCar.currentState.activePlayer

        rootService.cableCar.currentState.activePlayer.currentTile = GameTile(61, listOf(1, 0, 3, 2, 5, 4, 7, 6))

        assertTrue(rootService.aIService.isOnePointPosition(1, 1))
        assertThrows<IllegalArgumentException> { rootService.aIService.isOnePointPosition(0, 0) }
        assertFalse(rootService.aIService.isOnePointPosition(2, 4))
        assertTrue(rootService.aIService.only1PointPositions())
        assertTrue(rootService.aIService.placeablePosition(1, 5))
        assertFalse(rootService.aIService.placeablePosition(2, 5))

        rootService.cableCar.currentState.activePlayer.currentTile = GameTile(61, listOf(5, 2, 1, 4, 3, 0, 7, 6))
        assertFalse(rootService.aIService.placeablePosition(1, 5))
    }
}
