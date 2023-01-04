package service

import GameInitMessage
import TurnMessage
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
    val networkService: NetworkService,
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
     *
     */
    override fun onCreateGameResponse(response: CreateGameResponse) {
        when(response.status) {
            CreateGameResponseStatus.SUCCESS -> return
            CreateGameResponseStatus.ALREADY_ASSOCIATED_WITH_GAME -> return
            CreateGameResponseStatus.SESSION_WITH_ID_ALREADY_EXISTS -> return
            CreateGameResponseStatus.GAME_ID_DOES_NOT_EXIST -> return
            CreateGameResponseStatus.SERVER_ERROR -> return
        }
    }

    /**
     *
     */
    override fun onJoinGameResponse(response: JoinGameResponse) {
        // Wait for game to start
        when(response.status) {
            JoinGameResponseStatus.SUCCESS -> return
            JoinGameResponseStatus.ALREADY_ASSOCIATED_WITH_GAME -> return
            JoinGameResponseStatus.INVALID_SESSION_ID -> return
            JoinGameResponseStatus.PLAYER_NAME_ALREADY_TAKEN -> return
            JoinGameResponseStatus.SERVER_ERROR -> return
        }
    }

    /**
     *
     */
    override fun onPlayerJoined(notification: PlayerJoinedNotification) {
        // Update game lobby
    }

    /**
     *
     */
    override fun onPlayerLeft(notification: PlayerLeftNotification) {
        // Stop the game and show a message to the player
    }

    /**
     *
     */
    override fun onGameActionResponse(response: GameActionResponse) {
        // Update local game? Or just show success message?
    }

    /**
     *
     */
    @GameActionReceiver
    fun onGameInitMessageReceived(message: GameInitMessage, sender: String) {
        // if sender is the host, validate game tiles and create a game instance based on message data
    }

    /**
     *
     */
    @GameActionReceiver
    fun onTurnMessageReceived(message: TurnMessage, sender: String) {
        // if sender is the active player, perform his turn based on the message data
    }
}
