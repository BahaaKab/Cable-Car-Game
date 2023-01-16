package service

import edu.udo.cs.sopra.ntf.GameInitMessage
import edu.udo.cs.sopra.ntf.GameStateVerificationInfo
import edu.udo.cs.sopra.ntf.PlayerType
import edu.udo.cs.sopra.ntf.TurnMessage
import entity.PLAYER_ORDER_COLORS
import entity.PlayerInfo
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
import tools.aqua.bgw.net.common.response.GameActionResponse


// Secret is "cable22"

/**
 *
 */
class CableCarNetworkClient(
    private val networkService: NetworkService,
    playerName: String,
    host: String,
    secret: String
) : BoardGameClient(
    playerName,
    host,
    secret,
    NetworkLogging.VERBOSE
) {
    /**
     * Handle the [CreateGameResponse]. On success, show a game lobby as host.
     */
    override fun onCreateGameResponse(response: CreateGameResponse) = BoardGameApplication.runOnGUIThread {
        when(response.status) {
            CreateGameResponseStatus.SUCCESS -> {
                networkService.onAllRefreshables { refreshAfterHostGame() }
            }
            CreateGameResponseStatus.ALREADY_ASSOCIATED_WITH_GAME -> { }
            CreateGameResponseStatus.SESSION_WITH_ID_ALREADY_EXISTS -> { }
            CreateGameResponseStatus.GAME_ID_DOES_NOT_EXIST -> { }
            CreateGameResponseStatus.SERVER_ERROR -> { }
        }
    }

    /**
     * Handle the [JoinGameResponse]. On success, show a game lobby as guest.
     */
    override fun onJoinGameResponse(response: JoinGameResponse) = BoardGameApplication.runOnGUIThread {
        when(response.status) {
            JoinGameResponseStatus.SUCCESS -> {
                networkService.onAllRefreshables { refreshAfterJoinGame(response.opponents) }
            }
            JoinGameResponseStatus.ALREADY_ASSOCIATED_WITH_GAME -> { }
            JoinGameResponseStatus.INVALID_SESSION_ID -> { }
            JoinGameResponseStatus.PLAYER_NAME_ALREADY_TAKEN -> { }
            JoinGameResponseStatus.SERVER_ERROR -> { }
        }
    }

    /**
     * Handle the [PlayerJoinedNotification].
     */
    override fun onPlayerJoined(notification: PlayerJoinedNotification) = BoardGameApplication.runOnGUIThread {
        // Expecting, that the game hasn't started yet
        networkService.onAllRefreshables { refreshAfterGuestJoined(notification.sender) }
    }

    /**
     *
     */
    override fun onPlayerLeft(notification: PlayerLeftNotification) = BoardGameApplication.runOnGUIThread {
        // Expecting, that the game hasn't started yet
        networkService.onAllRefreshables { refreshAfterGuestLeft(notification.sender) }
    }

    /**
     *
     */
    override fun onGameActionResponse(response: GameActionResponse) {
        // TODO: Update local game? Or just show success message? What exactly should happen here?
    }

    /**
     *
     */
    @GameActionReceiver
    fun onGameInitMessageReceived(message: GameInitMessage, sender: String) {
        // if sender is the host, validate game tiles and create a game instance based on message data
        // TODO: How to properly check, that the game has not started and that the sender is actually the host?
        val playerInfos = message.players.mapIndexed { index, info ->
            val name = info.name
            //TODO: Not so nice, as we have to suggest an AI difficulty level
            val playerType = when(info.playerType) {
                PlayerType.AI -> entity.PlayerType.AI_HARD
                PlayerType.HUMAN -> entity.PlayerType.HUMAN
            }
            val color = PLAYER_ORDER_COLORS[index]

            PlayerInfo(name, playerType, color, isNetworkPlayer = true)
        }

        networkService.rootService.setupService.startNetworkGame(
            isHostPlayer = false,
            playerInfos = playerInfos,
            tilesRotatable = message.rotationAllowed,
            tileIDs = message.tileSupply.map { it.id },
            AISpeed = 1
        )
    }

    /**
     *
     */
    @GameActionReceiver
    fun onTurnMessageReceived(message: TurnMessage, sender: String) = with(networkService.rootService){
        val rotations = arrayOf(0, 90, 180, 270)
        require(message.rotation in rotations)
        // if sender is the active player, perform his turn based on the message data
        require(sender == cableCar.currentState.activePlayer.name)
        // Draw tile, if necessary
        if (message.fromSupply) {
            playerActionService.drawTile()
        }
        // Rotate tile
        repeat(rotations.indexOf(message.rotation) -1) {
            playerActionService.rotateTileRight()
        }
        // Place tile
        playerActionService.placeTile(message.posX, message.posY)
        // Validate the gameState
        check(gameStateIsValid(message.verifcationInfo))
    }

    /**
     *
     */
    private fun gameStateIsValid(gameStateVerificationInfo: GameStateVerificationInfo): Boolean {
        return true
    }
}
