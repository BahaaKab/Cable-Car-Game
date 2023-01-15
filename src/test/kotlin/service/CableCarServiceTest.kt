package service

import entity.*
import org.junit.jupiter.api.assertThrows
import kotlin.test.*

class CableCarServiceTest {

    @Test
    fun testNextTurn() {
        val rootService = RootService();

        val p1 = Player(PlayerType.HUMAN, Color.BLUE, "Player_1", listOf(), false)
        val p2 = Player(PlayerType.HUMAN, Color.GREEN, "Player_2", listOf(), false)
        val p3 = Player(PlayerType.HUMAN, Color.BLACK, "Player_3", listOf(), false)

        val testBoard : Array<Array<Tile?>> = Array(10) {
            Array(10) { null }
        }

        val state = State(rootService.ioService.getTilesFromCSV().toMutableList(), p1, testBoard, listOf(p1,p2,p3))

        rootService.cableCar = CableCar(false, 10, false, GameMode.HOTSEAT,
                                        History(), state)

        assertEquals(p1,rootService.cableCar.currentState.activePlayer)
        rootService.cableCarService.nextTurn()
        assertEquals(p2,rootService.cableCar.currentState.activePlayer)

    }

    @Test
    fun testCalculateWinners() {
        val rootService = RootService();

        val p1 = Player(PlayerType.HUMAN, Color.BLUE, "Player_1", listOf(), false)
        val p2 = Player(PlayerType.HUMAN, Color.GREEN, "Player_2", listOf(), false)
        val p3 = Player(PlayerType.HUMAN, Color.BLACK, "Player_3", listOf(), false)

        val testBoard : Array<Array<Tile?>> = Array(10) {
            Array(10) { null }
        }

        val state = State(rootService.ioService.getTilesFromCSV().toMutableList(), p1, testBoard, listOf(p1,p2,p3))

        rootService.cableCar = CableCar(false, 10, false, GameMode.HOTSEAT,
            History(), state)

        p1.score = 15
        p2.score = 14
        p3.score = 17

        val listOfWinners : List<Player> = rootService.cableCarService.calculateWinners()
        assertEquals(p3,listOfWinners[0])
    }
}