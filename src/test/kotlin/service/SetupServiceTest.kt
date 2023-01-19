package service

import entity.*
import org.junit.jupiter.api.assertThrows
import java.lang.IllegalArgumentException
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/**
 * Test the [SetupService] class.
 */
class SetupServiceTest {
    private val twoPlayersStationTileConfig = listOf(
        listOf(1, 3, 5, 7, 9, 11, 13, 15, 17, 19, 21, 23, 25, 27, 29, 31),
        listOf(2, 4, 6, 8, 10, 12, 14, 16, 18, 20, 22, 24, 26, 28, 30, 32)
    )
    private val threePlayersStationTileConfig = listOf(
        listOf(1, 4, 6, 11, 15, 20, 23, 25, 28, 31),
        listOf(2, 7, 9, 12, 14, 19, 22, 27, 29, 32),
        listOf(3, 5, 8, 10, 13, 18, 21, 24, 26, 30)
    )
    private val fourPlayersStationTileConfig = listOf(
        listOf(4, 7, 11, 16, 20, 23, 27, 32),
        listOf(3, 8, 12, 15, 19, 24, 28, 31),
        listOf(1, 6, 10, 13, 18, 21, 25, 30),
        listOf(2, 5, 9, 14, 17, 22, 26, 29),
    )
    private val fivePlayersStationTileConfig = listOf(
        listOf(1, 5, 10, 14, 22, 28),
        listOf(6, 12, 18, 23, 27, 32),
        listOf(3, 7, 15, 19, 25, 29),
        listOf(2, 9, 13, 21, 26, 30),
        listOf(4, 8, 11, 20, 24, 31)
    )
    private val sixPlayersStationTileConfig = listOf(
        listOf(1, 5, 10, 19, 27),
        listOf(2, 11, 18, 25, 29),
        listOf(4, 8, 14, 21, 26),
        listOf(6, 15, 20, 24, 31),
        listOf(3, 9, 13, 23, 30),
        listOf(7, 12, 22, 28, 32)
    )
    private val stationTileConfigs = listOf(
        twoPlayersStationTileConfig,
        threePlayersStationTileConfig,
        fourPlayersStationTileConfig,
        fivePlayersStationTileConfig,
        sixPlayersStationTileConfig
    )

    /**
     * Test the initialization of a local game.
     */
    @Test
    fun testStartLocalGame() = (2..6).forEach { i ->
        val rootService = RootService()
        val playerInfos = generatePlayerInfos(i)
        rootService.setupService.startLocalGame(
            playerInfos,
            false,
            1
        )
        with(rootService.cableCar) {
            // Check general setup
            assertFalse(allowTileRotation)
            assertEquals(1, AISpeed)
            assertEquals(GameMode.HOTSEAT, gameMode)
            // Check, if players match playerInfos
            assertPlayersEqualPlayerInfos(currentState, playerInfos)
            // Check, if station tiles are assigned correctly
            assertValidStationTileAssignments(currentState)
            // Check, if the drawPile was initialized correctly
            assertValidDrawPile(currentState)
        }
    }

    /**
     * Test the initialization of a network game as host.
     */
    @Test
    fun testStartNetworkGameAsHost() = (2..6).forEach { i ->
        val rootService = RootService()
        val playerInfos = generatePlayerInfos(i)
        rootService.setupService.startNetworkGame(
            true,
            playerInfos,
            false,
            null,
            1
        )
        with(rootService.cableCar) {
            assertFalse(allowTileRotation)
            assertEquals(1, AISpeed)
            assertEquals(GameMode.NETWORK, gameMode)
            assertTrue(isHostPlayer)
            // Check, if players match playerInfos
            assertPlayersEqualPlayerInfos(currentState, playerInfos)
            // Check, if station tiles are assigned correctly
            assertValidStationTileAssignments(currentState)
            // Check, if the drawPile was initialized correctly
            assertValidDrawPile(currentState)
        }
    }

    /**
     * Test the initialization of a network game as guest.
     */
    @Test
    fun testStartNetworkGameAsGuest() = (2..6).forEach { i ->
        val rootService = RootService()
        val playerInfos = generatePlayerInfos(i)
        // Simulate a card deck passed from the network host
        val drawPile = rootService.ioService.getTilesFromCSV().shuffled()
        rootService.setupService.startNetworkGame(
            false,
            playerInfos,
            false,
            drawPile.map { it.id },
            1
        )
        with(rootService.cableCar) {
            assertFalse(allowTileRotation)
            assertEquals(1, AISpeed)
            assertEquals(GameMode.NETWORK, gameMode)
            assertFalse(isHostPlayer)
            // Check, if players match playerInfos
            assertPlayersEqualPlayerInfos(currentState, playerInfos)
            // Check, if station tiles are assigned correctly
            assertValidStationTileAssignments(currentState)
            // Check, if the drawPile was initialized correctly
            assertValidDrawPile(currentState)
            // Additionally check, if the tiles where passed in the correct order
            currentState.players.zip(drawPile) { player, expectedTile ->
                assertEquals(expectedTile.id, player.handTile?.id)
            }
        }
    }

    /**
     * Test the initialization of a game instance with bad arguments.
     */
    @Test
    fun testStartGameInvalid() {
        // Test bad arguments for playerInfo amount
        val invalidNumberOfPlayersConfigs = listOf(0, 1, 7)
        val tileIDs = List(60) { it }
        invalidNumberOfPlayersConfigs.forEach {
            val playerInfos = generatePlayerInfos(it)
            assertThrows<IllegalArgumentException> {
                RootService().setupService.startLocalGame(playerInfos, true, 1)
            }
            assertThrows<IllegalArgumentException> {
                RootService().setupService.startNetworkGame(
                    true, playerInfos, true, null, 1
                )
            }
            assertThrows<IllegalArgumentException> {
                RootService().setupService.startNetworkGame(
                    false, playerInfos, true, tileIDs, 1
                )
            }
        }
        // Test bad arguments for tileIDs in network guest mode
        // No tileIds passed
        val playerInfos = generatePlayerInfos(4)
        assertThrows<IllegalArgumentException> {
            RootService().setupService.startNetworkGame(
                false, playerInfos, true, null, 1
            )
        }
        // Bad number of tileIDs passed
        listOf(59, 61).forEach { badNumberOfTileIDs ->
            val badTileIDs = List(badNumberOfTileIDs) { it }
            assertThrows<IllegalArgumentException> {
                RootService().setupService.startNetworkGame(
                    false, playerInfos, true, badTileIDs, 1
                )
            }
        }
    }

    /**
     * Throw an exception, if the draw pile has not the expected size, after tiles where initially passed to the players.
     *
     * @param state A game state
     */
    private fun assertValidDrawPile(state: State) {
        val expectedDrawPileSize = 60 - state.players.size
        assertEquals(expectedDrawPileSize, state.drawPile.size)
    }

    /**
     * Throw an exception, if the players of the state do not match the given player infos.
     *
     * @param state A game state
     * @param playerInfos The player infos to test against
     */
    private fun assertPlayersEqualPlayerInfos(state: State, playerInfos: List<PlayerInfo>) {
        assertEquals(playerInfos.size, state.players.size)
        state.players.zip(playerInfos) { player, playerInfo ->
            assertEquals(playerInfo.name, player.name)
            assertEquals(playerInfo.color, player.color)
            assertEquals(playerInfo.isNetworkPlayer, player.isNetworkPlayer)
            assertEquals(playerInfo.playerType, player.playerType)
        }
    }

    /**
     * Throw an error, if the station tiles assigned the players do not match the configuration of the rulebook.
     *
     * @param state The game state to test.
     */
    private fun assertValidStationTileAssignments(state: State) {
        val stationTilesFromBoard = getStationTilesFromBoard(state.board)
        state.players.forEachIndexed { i, player ->
            val expectedTileIds = getStationTilesIdConfig(state.players.size, i)
            val currentTileIds = stationTilesFromBoard.mapIndexed{ j, tile ->
                if (tile in player.stationTiles) j + 1 else -1
            }.filter { it > 0 }

            assertEquals(expectedTileIds.size, currentTileIds.size)
            expectedTileIds.zip(currentTileIds){ expectedTile, currentTile ->
                assertEquals(
                    expectedTile,
                    currentTile,
                    "Number of Players: ${state.players.size}, Player number: $i")
            }
        }
    }

    /**
     * Get the station tile assignments as ids following the specifications of the rulebook.
     *
     * @param numberOfPlayers The number of players
     * @param playerPosition The turn position of the player
     *
     * @throws IllegalArgumentException If the number of players is not valid or the player position is bigger than
     * the amount of players.
     * @return The station tiles assigned to the player at [playerPosition] for the configuration of [numberOfPlayers].
     */
    private fun getStationTilesIdConfig(numberOfPlayers: Int, playerPosition: Int) : List<Int> {
        require(numberOfPlayers in 2..6)
        require(playerPosition in 0 until numberOfPlayers)
        return stationTileConfigs[numberOfPlayers - 2][playerPosition]
    }

    /**
     * Return the station tiles in the order of the station tile IDs.
     *
     * @param board The game board
     *
     * @return The station tiles ordered after their Ids, starting in the upper left corner.
     * Therefore the index of a tile equals the (id - 1).
     */
    private fun getStationTilesFromBoard(board: Array<Array<Tile?>>) : List<StationTile> {
        val stationTiles = mutableListOf<StationTile>()
        for (i in 1..8) {
            stationTiles.add(board[i][0] as StationTile)
        }
        for (i in 1..8) {
            stationTiles.add(board[9][i] as StationTile)
        }
        for (i in 8 downTo 1) {
            stationTiles.add(board[i][9] as StationTile)
        }
        for (i in 8 downTo 1) {
            stationTiles.add(board[0][i] as StationTile)
        }
        return stationTiles
    }

    /**
     * Generate player infos.
     *
     * @param numberOfPlayers The number of players. Should be less or equal than 6.
     *
     * @throws IllegalArgumentException If [numberOfPlayers] is bigger than 6
     * @return The player infos
     */
    private fun generatePlayerInfos(numberOfPlayers: Int) : List<PlayerInfo> {
        val colors = arrayOf(Color.YELLOW, Color.BLUE, Color.ORANGE, Color.GREEN, Color.PURPLE, Color.BLACK)
        return List(numberOfPlayers) { i ->
            PlayerInfo("Player$i", PlayerType.HUMAN, colors[i % colors.size], false)
        }
    }

}