package service

import entity.*
import kotlin.test.*

class CableCarServiceTest {

    /**
     * Test to check if the next active [Player] is set correctly if the last active [Player]
     * wasn't the last player in the playerlist
     */
    @Test
    fun testNextTurn1() {
        val rootService = RootService()

        val p1 = Player(PlayerType.HUMAN, Color.BLUE, "Player_1", listOf(), false)
        val p2 = Player(PlayerType.HUMAN, Color.GREEN, "Player_2", listOf(), false)
        val p3 = Player(PlayerType.HUMAN, Color.BLACK, "Player_3", listOf(), false)

        val testBoard: Array<Array<Tile?>> = Array(10) {
            Array(10) { null }
        }

        val state = State(rootService.ioService.getTilesFromCSV().toMutableList(), p1, testBoard, listOf(p1, p2, p3))

        rootService.cableCar = CableCar(
            false, 10, false, GameMode.HOTSEAT,
            History(), state
        )

        assertEquals(p1, rootService.cableCar.currentState.activePlayer)
        rootService.cableCarService.nextTurn()
        assertEquals(p2, rootService.cableCar.currentState.activePlayer)

    }

    /**
     * Test to check if the next active [Player] is set correctly if the last active [Player]
     * was the last player in the playerlist
     */
    @Test
    fun testNextTurn2() {
        val rootService = RootService()

        val p1 = Player(PlayerType.HUMAN, Color.BLUE, "Player_1", listOf(), false)
        val p2 = Player(PlayerType.HUMAN, Color.GREEN, "Player_2", listOf(), false)
        val p3 = Player(PlayerType.HUMAN, Color.BLACK, "Player_3", listOf(), false)

        val testBoard: Array<Array<Tile?>> = Array(10) {
            Array(10) { null }
        }

        val state = State(rootService.ioService.getTilesFromCSV().toMutableList(), p3, testBoard, listOf(p1, p2, p3))

        rootService.cableCar = CableCar(
            false, 10, false, GameMode.HOTSEAT,
            History(), state
        )

        assertEquals(p3, rootService.cableCar.currentState.activePlayer)
        rootService.cableCarService.nextTurn()
        assertEquals(p1, rootService.cableCar.currentState.activePlayer)

    }

    /**
     * Test to check if the winners of the game are calculated correctly by the points
     */
    @Test
    fun testCalculateWinners() {
        val rootService = RootService()

        val p1 = Player(PlayerType.HUMAN, Color.BLUE, "Player_1", listOf(), false)
        val p2 = Player(PlayerType.HUMAN, Color.GREEN, "Player_2", listOf(), false)
        val p3 = Player(PlayerType.HUMAN, Color.BLACK, "Player_3", listOf(), false)

        val testBoard: Array<Array<Tile?>> = Array(10) {
            Array(10) { null }
        }

        val state = State(rootService.ioService.getTilesFromCSV().toMutableList(), p1, testBoard, listOf(p1, p2, p3))

        rootService.cableCar = CableCar(
            false, 10, false, GameMode.HOTSEAT,
            History(), state
        )

        p1.score = 15
        p2.score = 14
        p3.score = 17

        val listOfWinners: List<Player> = rootService.cableCarService.calculateWinners()
        assertEquals(1, listOfWinners.size)
        assertEquals(p3, listOfWinners[0])
    }

    /**
     * Test to check if the positions of a [StationTile] in the grid are calculated correctly
     */
    @Test
    fun testGetPosition() {
        val rootService = RootService()

        val p1 = Player(PlayerType.HUMAN, Color.BLUE, "Player_1", listOf(), false)
        val testBoard: Array<Array<Tile?>> = Array(10) {
            Array(10) { null }
        }
        val state = State(rootService.ioService.getTilesFromCSV().toMutableList(), p1, testBoard, listOf(p1))
        rootService.cableCar = CableCar(
            false, 10, false, GameMode.HOTSEAT,
            History(), state
        )

        val stationTile = StationTile(listOf(0, 1))
        testBoard[0][5] = stationTile

        val positionArr = rootService.cableCarService.getPosition(stationTile)
        assertEquals(0, positionArr[0])
        assertEquals(5, positionArr[1])
    }

    /**
     * Test to check if the path of a [StationTile] gets updated correctly if a [GameTile] was placed that gets included
     * in the path.
     * You can construct the test also by a drawn example, and it should be correct.
     */
    @Test
    fun testUpdatePath() {
        val rootService = RootService()

        val p1 = Player(PlayerType.HUMAN, Color.BLUE, "Player_1", listOf(), false)
        val testBoard: Array<Array<Tile?>> = Array(10) {
            Array(10) { null }
        }
        val state = State(rootService.ioService.getTilesFromCSV().toMutableList(), p1, testBoard, listOf(p1))
        rootService.cableCar = CableCar(
            false, 10, false, GameMode.HOTSEAT,
            History(), state
        )
        // Two [StationTiles] nearby get connected by two [GameTiles]
        val beginStationTile = StationTile(listOf(0, 1))
        val endStationTile = StationTile(listOf(0, 1))
        testBoard[4][9] = beginStationTile
        testBoard[5][9] = endStationTile

        val gameTile1 = GameTile(1, listOf(7, 6, 4, 5, 2, 3, 1, 0))
        val gameTile2 = GameTile(2, listOf(1, 0, 3, 2, 6, 7, 4, 5))
        testBoard[4][8] = gameTile1
        testBoard[5][8] = gameTile2
        // Without the update the size of both paths should be 0
        assertEquals(0, beginStationTile.path.size)
        assertEquals(0, endStationTile.path.size)

        rootService.cableCarService.updatePath(beginStationTile, p1.color)
        rootService.cableCarService.updatePath(endStationTile, p1.color)
        // After the update the two [StationTiles] are connected by two different paths
        assertEquals(gameTile1, beginStationTile.path[0])
        assertEquals(gameTile2, beginStationTile.path[1])
        assertEquals(endStationTile, beginStationTile.path[2])

        assertEquals(gameTile2, endStationTile.path[0])
        assertEquals(gameTile1, endStationTile.path[1])
        assertEquals(beginStationTile, endStationTile.path[2])
    }

    /**
     * Test to check if the paths of the [Player]s get updated after placing a [GameTile] on the grid.
     * It simulates a small game where two [GameTile]s get placed in succession.
     * You can also construct the test by a drawn example, and it should be correct.
     */
    @Test
    fun testUpdatePaths() {
        val rootService = RootService()

        val stationTileOfP1 = StationTile(listOf(0, 1))
        val stationTileOfP2 = StationTile(listOf(0, 1))
        val p1 = Player(PlayerType.HUMAN, Color.BLUE, "Player_1", listOf(stationTileOfP1), false)
        val p2 = Player(PlayerType.HUMAN, Color.GREEN, "Player_2", listOf(stationTileOfP2), false)
        val testBoard: Array<Array<Tile?>> = Array(10) {
            Array(10) { null }
        }
        val state = State(rootService.ioService.getTilesFromCSV().toMutableList(), p1, testBoard, listOf(p1, p2))
        rootService.cableCar = CableCar(
            false, 10, false, GameMode.HOTSEAT,
            History(), state
        )

        testBoard[4][9] = stationTileOfP1
        testBoard[5][9] = stationTileOfP2
        // First [GameTile] gets placed
        val firstPlacedGameTile = GameTile(1, listOf(7, 6, 4, 5, 2, 3, 1, 0))
        testBoard[4][8] = firstPlacedGameTile
        // Before the update the path sizes of the players should be 0
        assertEquals(0, p1.stationTiles[0].path.size)
        assertEquals(0, p2.stationTiles[0].path.size)

        rootService.cableCarService.updatePaths(4, 8)
        // After the update the path of [Player] 1 should have one [GameTile] included
        assertEquals(firstPlacedGameTile, p1.stationTiles[0].path[0])
        assertEquals(0, p2.stationTiles[0].path.size)
        // second [GameTile] gets placed
        val secondPlacedGameTile = GameTile(2, listOf(1, 0, 3, 2, 6, 7, 4, 5))
        testBoard[5][8] = secondPlacedGameTile

        rootService.cableCarService.updatePaths(5, 8)
        // After the update the [Player]s paths should be closed
        assertEquals(firstPlacedGameTile, p1.stationTiles[0].path[0])
        assertEquals(secondPlacedGameTile, p1.stationTiles[0].path[1])
        assertEquals(stationTileOfP2, p1.stationTiles[0].path[2])

        assertEquals(secondPlacedGameTile, p2.stationTiles[0].path[0])
        assertEquals(firstPlacedGameTile, p2.stationTiles[0].path[1])
        assertEquals(stationTileOfP1, p2.stationTiles[0].path[2])
    }

    /**
     * Test to check if the calculation of points of the [Player]s works correctly. It simulates a small game where
     * after placing a [GameTile] the paths of [Player]s get updated and after that the points get calculated.
     * In this test only the paths between two [StationTile]s get tested.
     * You can also construct the test as a drawn example, and it should be correct.
     */
    @Test
    fun testCalculatePoints1() {
        val rootService = RootService()

        val stationTileOfP1 = StationTile(listOf(0, 1))
        val stationTileOfP2 = StationTile(listOf(0, 1))
        val p1 = Player(PlayerType.HUMAN, Color.BLUE, "Player_1", listOf(stationTileOfP1), false)
        val p2 = Player(PlayerType.HUMAN, Color.GREEN, "Player_2", listOf(stationTileOfP2), false)
        val testBoard: Array<Array<Tile?>> = Array(10) {
            Array(10) { null }
        }
        val state = State(rootService.ioService.getTilesFromCSV().toMutableList(), p1, testBoard, listOf(p1, p2))
        rootService.cableCar = CableCar(
            false, 10, false, GameMode.HOTSEAT,
            History(), state
        )

        testBoard[4][9] = stationTileOfP1
        testBoard[5][9] = stationTileOfP2
        // First [GameTile] gets placed
        val firstPlacedGameTile = GameTile(1, listOf(7, 6, 4, 5, 2, 3, 1, 0))
        testBoard[4][8] = firstPlacedGameTile

        rootService.cableCarService.updatePaths(4, 8)
        rootService.cableCarService.calculatePoints()
        // In this case the points of the [Player]s should be 0
        assertEquals(0, p1.score)
        assertEquals(0, p2.score)
        // second [GameTile] gets placed
        val secondPlacedGameTile = GameTile(2, listOf(1, 0, 3, 2, 6, 7, 4, 5))
        testBoard[5][8] = secondPlacedGameTile

        rootService.cableCarService.updatePaths(5, 8)
        rootService.cableCarService.calculatePoints()
        // In this case the two paths of the [Player]s are closed and should give them 2 points
        assertEquals(2, p1.score)
        assertEquals(2, p2.score)
    }

    /**
     * Test to check if the calculation of points of the [Player]s works correctly. It simulates a small game where
     * after placing a [GameTile] the paths of [Player]s get updated and after that the points get calculated.
     * In this test only the path to a [PowerStationTile] gets tested.
     * You can also construct the test as a drawn example, and it should be correct.
     */
    @Test
    fun testCalculatePoints2() {
        val rootService = RootService()

        val stationTileOfP1 = StationTile(listOf(0, 1))
        val powerStationTile = PowerStationTile(listOf(4, 5, 6, 7))
        val p1 = Player(PlayerType.HUMAN, Color.BLUE, "Player_1", listOf(stationTileOfP1), false)
        val testBoard: Array<Array<Tile?>> = Array(10) {
            Array(10) { null }
        }
        val state = State(rootService.ioService.getTilesFromCSV().toMutableList(), p1, testBoard, listOf(p1))
        rootService.cableCar = CableCar(
            false, 10, false, GameMode.HOTSEAT,
            History(), state
        )

        testBoard[4][9] = stationTileOfP1
        testBoard[4][5] = powerStationTile

        val gameTile1 = GameTile(1, listOf(5, 4, 7, 6, 1, 0, 3, 2))
        val gameTile2 = GameTile(2, listOf(5, 4, 7, 6, 1, 0, 3, 2))
        val gameTile3 = GameTile(3, listOf(5, 4, 7, 6, 1, 0, 3, 2))
        // First [GameTile] gets placed
        testBoard[4][8] = gameTile1
        rootService.cableCarService.updatePaths(4, 8)
        rootService.cableCarService.calculatePoints()
        // Path shouldn't be closed and should gain 0 points
        assertEquals(0, p1.score)
        // Second [GameTile] gets placed
        testBoard[4][7] = gameTile2
        rootService.cableCarService.updatePaths(4, 7)
        rootService.cableCarService.calculatePoints()
        // Path shouldn't be closed and should gain 0 points
        assertEquals(0, p1.score)
        // Third [GameTile] gets placed
        testBoard[4][6] = gameTile3
        rootService.cableCarService.updatePaths(4, 6)
        rootService.cableCarService.calculatePoints()
        // Because the path is closed by a [PowerStationTile] and has length 3 the gain of points should be 6 (2*3)
        assertEquals(6, p1.score)
    }
}