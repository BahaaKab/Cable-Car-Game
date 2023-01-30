package service

import edu.udo.cs.sopra.ntf.GameInitMessage
import edu.udo.cs.sopra.ntf.GameStateVerificationInfo
import edu.udo.cs.sopra.ntf.TurnMessage
import entity.PLAYER_ORDER_COLORS
import entity.PlayerInfo
import entity.PlayerType
import entity.State
import tools.aqua.bgw.core.BoardGameApplication
import tools.aqua.bgw.net.client.BoardGameClient
import tools.aqua.bgw.net.client.NetworkLogging
import tools.aqua.bgw.net.common.annotations.GameActionReceiver
import tools.aqua.bgw.net.common.notification.PlayerJoinedNotification
import tools.aqua.bgw.net.common.notification.PlayerLeftNotification
import tools.aqua.bgw.net.common.response.CreateGameResponse
import tools.aqua.bgw.net.common.response.CreateGameResponseStatus
import tools.aqua.bgw.net.common.response.JoinGameResponse
import tools.aqua.bgw.net.common.response.JoinGameResponseStatus


/**
 * Client to receive and handle network messages from the server.
 *
 * @property networkService The network service instance
 * @param playerName The player name
 * @param host The host server address
 * @param secret The secret to connect with the host server
 */
class CableCarNetworkClient(
    private val networkService: NetworkService,
    playerName: String,
    val playerType: PlayerType,
    host: String,
    secret: String
) : BoardGameClient(
    playerName,
    host,
    secret,
    NetworkLogging.INFO
) {
    /**
     * Handle the [CreateGameResponse]. On success, open a game lobby as host.
     *
     * @param response A [CreateGameResponse]
     */
    override fun onCreateGameResponse(response: CreateGameResponse) = BoardGameApplication.runOnGUIThread {
        networkService.onAllRefreshables { refreshAfterNetworkResponse(response) }
        when (response.status) {
            CreateGameResponseStatus.SUCCESS -> {
                networkService.onAllRefreshables { refreshAfterHostGame() }
            }
            else -> {}
        }
    }

    /**
     * Handle the [JoinGameResponse]. On success, open a game lobby as guest.
     *
     * @param response A [JoinGameResponse]
     */
    override fun onJoinGameResponse(response: JoinGameResponse) = BoardGameApplication.runOnGUIThread {
        networkService.onAllRefreshables { refreshAfterNetworkResponse(response) }
        when (response.status) {
            JoinGameResponseStatus.SUCCESS -> {
                networkService.onAllRefreshables { refreshAfterJoinGame(response.opponents) }
            }
            else -> {}
        }
    }

    /**
     * Handle a [PlayerJoinedNotification].
     *
     * @param notification A [PlayerJoinedNotification]
     */
    override fun onPlayerJoined(notification: PlayerJoinedNotification) = BoardGameApplication.runOnGUIThread {
        networkService.onAllRefreshables { refreshAfterNetworkNotification(notification) }
        check(!networkService.rootService.isGameInitialized())
        networkService.onAllRefreshables { refreshAfterGuestJoined(notification.sender) }
    }

    /**
     * Handle a [PlayerLeftNotification].
     *
     * @param notification A [PlayerLeftNotification]
     */
    override fun onPlayerLeft(notification: PlayerLeftNotification) = BoardGameApplication.runOnGUIThread {
        networkService.onAllRefreshables { refreshAfterNetworkNotification(notification) }
        networkService.onAllRefreshables { refreshAfterGuestLeft(notification.sender) }
        if (networkService.rootService.isGameInitialized()) networkService.disconnect()
    }

    /**
     * Handle a [GameInitMessage]
     *
     * @param message A [GameInitMessage]
     * @param sender The name of the sender
     */
    @Suppress("UNUSED", "UNUSED_PARAMETER")
    @GameActionReceiver
    fun onGameInitMessageReceived(message: GameInitMessage, sender: String) {
        // If the local player is the host, throw an exception, as the sender cannot be the host
        check(!networkService.rootService.isGameInitialized() || !networkService.rootService.cableCar.isHostPlayer)

        val playerInfos = message.players.mapIndexed { index, info ->
            val name = info.name
            val color = PLAYER_ORDER_COLORS[index]
            val playerType = if (name != playerName) PlayerType.HUMAN else this.playerType
            val isNetworkPlayer = name != playerName
            PlayerInfo(name, playerType, color, isNetworkPlayer)
        }

        networkService.rootService.setupService.startNetworkGame(
            isHostPlayer = false,
            playerInfos = playerInfos,
            tilesRotatable = message.rotationAllowed,
            tileIDs = message.tileSupply.map { it.id },
            aiSpeed = 1
        )
    }

    /**
     * Handle a [TurnMessage]
     *
     * @param message A [TurnMessage]
     * @param sender The name of the sender
     */
    @Suppress("UNUSED")
    @GameActionReceiver
    fun onTurnMessageReceived(message: TurnMessage, sender: String) = with(networkService.rootService) {
        BoardGameApplication.runOnGUIThread {
            // if sender is the active player, perform his turn based on the message data
            println("sender: $sender, activePlayer: ${cableCar.currentState.activePlayer.name}")

            require(sender == cableCar.currentState.activePlayer.name)
            // Draw tile, if necessary
            if (message.fromSupply) {
                playerActionService.drawTile()
            }
            // Rotate tile
            if (cableCar.allowTileRotation) {
                val rotations = intArrayOf(0, 90, 180, 270)
                require(message.rotation in rotations)
                repeat(rotations.indexOf(message.rotation)) {
                    playerActionService.rotateTileRight()
                }
            }
            // Place tile
            playerActionService.placeTile(message.posX, message.posY)

            // Validate the gameState
            check(isValidGameState(message.gameStateVerificationInfo))
        }
    }

    /**
     * Verify that the local [State] of the game at the end of a turn matches the state of the host player.
     *
     * @param gameStateVerificationInfo The host player's game state at the end of the turn
     *
     * @returns Whether the local state matches the host player's state.
     */
    private fun isValidGameState(gameStateVerificationInfo: GameStateVerificationInfo): Boolean =
        with(gameStateVerificationInfo) {
            val currentState = networkService.rootService.cableCar.currentState
            val drawPileIds = currentState.drawPile.map { it.id }
            val isValidDrawPile = supply == drawPileIds
            val isValidPlacedTiles = placedTiles.sortedBy { it.id } == currentState.placedTiles.sortedBy { it.id }
            val isValidPlayerScores = playerScores == currentState.players.map { it.score }
            return isValidDrawPile && isValidPlacedTiles && isValidPlayerScores
        }
}
