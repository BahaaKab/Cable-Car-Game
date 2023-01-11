package service

import entity.*
import kotlin.IllegalArgumentException

class SetupService(private val rootService: RootService) : AbstractRefreshingService() {

    /**
     * Creates and starts a new local game in hotseat mode.
     * **/
    fun startLocalGame(playerInfos: List<PlayerInfo>, tilesRotatable: Boolean, AISpeed: Int) {
        val drawPile = rootService.ioService.getTilesFromCSV().shuffled().toMutableList()
        val board = createBoard()
        val players = createPlayers(playerInfos, board)
        // Give each player a game tile from the draw pile
        players.forEach { it.handTile = drawPile.removeFirst() }

        val initialState = State(
            drawPile = drawPile,
            activePlayer = players.first(),
            board = board,
            players = players
        )

        rootService.cableCar = CableCar(
            allowTileRotation = tilesRotatable,
            AISpeed = AISpeed,
            isHostPlayer = false,
            gameMode = GameMode.HOTSEAT,
            history = History(),
            currentState = initialState
        )

        onAllRefreshables { refreshAfterStartGame() }
    }

    /**
     *
     */
    fun startNetworkGame(isHostPlayer: Boolean,
        playerInfos: List<PlayerInfo>,
        tilesRotatable: Boolean,
        tileIDs: List<Int>?,
        AISpeed: Int
    ) {
        val drawPile = if (isHostPlayer) {
            rootService.ioService.getTilesFromCSV().shuffled()
        } else {
            // If the game is hosted by another player, sync the draw pile via the tileIDs passed through
            // the GameInitMessage
            requireNotNull(tileIDs)
            val gameTiles = rootService.ioService.getTilesFromCSV().sortedBy { it.id }
            tileIDs.map{ gameTiles[it] }
        }.toMutableList()

        val board = createBoard()
        val players = createPlayers(playerInfos, board)
        // Give each player a game tile from the draw pile
        players.forEach { it.handTile = drawPile.removeFirst() }

        val initialState = State(
            drawPile = drawPile,
            activePlayer = players.first(),
            board = board,
            players = players
        )

        rootService.cableCar = CableCar(
            allowTileRotation = tilesRotatable,
            AISpeed = AISpeed,
            isHostPlayer = isHostPlayer,
            gameMode = GameMode.NETWORK,
            history = History(),
            currentState = initialState
        )

        if (isHostPlayer) {
            // TODO: sendGameInitMessage()
        }
        onAllRefreshables { refreshAfterStartGame() }
    }

    /**
     *
     */
    private fun createPlayers(playerInfos: List<PlayerInfo>, board: Array<Array<Tile?>>) : List<Player> {
        val stationTileAssignments = getStationTileAssignments(playerInfos.size)
        return playerInfos.mapIndexed { i, playerInfo ->
            val stationTiles = stationTileAssignments[i].map { getStationTileFromId(it, board) }
            Player(playerInfo, stationTiles)
        }
    }

    /**
     *
     */
    private fun getStationTileFromId(id: Int, board: Array<Array<Tile?>>) : StationTile {
        val stationTile = when(id) {
            1, 2, 3, 4, 5, 6, 7, 8 -> board[id][0]
            9, 10, 11, 12, 13, 14, 15, 16 -> board[9][id - 8]
            17, 18, 19, 20, 21, 22, 23, 24 -> board[id - 16][9]
            25, 26, 27, 28, 29, 30, 31, 32 -> board[0][id - 24]
            else -> throw IllegalArgumentException("id has to be between 1 and 32, not $id.")
        }
        return checkNotNull(stationTile) as StationTile
    }

    /**
     *
     */
    private fun getStationTileAssignments(numberOfPlayers: Int) : List<List<Int>> = when(numberOfPlayers) {
        2 -> listOf(
            (1..31 step 2).toList(),
            (2..32 step 2).toList()
        )
        3 -> listOf(
            listOf(1, 4, 6, 11, 15, 20, 23, 25, 28, 31),
            listOf(2, 7, 9, 12, 14, 19, 22, 27, 29, 32),
            listOf(3, 5, 8, 10, 13, 18, 21, 24, 26, 30)
        )
        4 -> listOf(
            listOf(4, 7, 11, 16, 20, 23, 27, 32),
            listOf(3, 8, 12, 15, 19, 24, 28, 31),
            listOf(1, 6, 10, 13, 18, 21, 25, 30),
            listOf(2, 5, 9, 14, 17, 22, 26, 29)
        )
        5 -> listOf(
            listOf(1, 5, 10, 14, 22, 28),
            listOf(6, 12, 18, 23, 27, 32),
            listOf(3, 7, 15, 19, 25, 29),
            listOf(2, 9, 13, 21, 26, 30),
            listOf(4, 8, 11, 20, 24, 31)
        )
        6 -> listOf(
            listOf(1, 5, 10, 19, 27),
            listOf(2, 11, 18, 25, 29),
            listOf(4, 8, 14, 21, 26),
            listOf(6, 15, 20, 24, 31),
            listOf(3, 9, 13, 23, 30),
            listOf(7, 12, 22, 28, 32)
        )
        else -> throw IllegalArgumentException("numberOfPlayers has to be between 2 and 6, not $numberOfPlayers.")
    }

    /**
     *
     */
    private fun createBoard() : Array<Array<Tile?>> {
        // Create an empty board
        val board = Array<Array<Tile?>>(10) {
            Array(10) { null }
        }
        // Add the stationTiles...
        for (i in 2..8) {
            // ... on the left side
            board[0][i] = StationTile(listOf(RIGHT_TOP, RIGHT_BOT))
            // .. on the right side
            board[9][i] = StationTile(listOf(LEFT_TOP, LEFT_BOT))
            // .. on the top side
            board[i][0] = StationTile(listOf(BOT_LEFT, BOT_RIGHT))
            // .. on the bottom side
            board[i][9] = StationTile(listOf(TOP_LEFT, TOP_RIGHT))
        }
        // Add the empty corner tiles
        board[0][0] = CornerTile()
        board[0][9] = CornerTile()
        board[9][0] = CornerTile()
        board[9][9] = CornerTile()
        // Add the power station tiles
        board[4][4] = PowerStationTile(listOf(LEFT_BOT, LEFT_TOP, TOP_LEFT, TOP_RIGHT))
        board[5][4] = PowerStationTile(listOf(TOP_LEFT, TOP_RIGHT, RIGHT_TOP, RIGHT_BOT))
        board[5][5] = PowerStationTile(listOf(RIGHT_TOP, RIGHT_BOT, BOT_RIGHT, BOT_LEFT))
        board[4][5] = PowerStationTile(listOf(BOT_RIGHT, BOT_LEFT, LEFT_BOT, LEFT_TOP))

        return board
    }
}