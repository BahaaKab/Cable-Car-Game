package service

import GameInitMessage
import TurnMessage
import tools.aqua.bgw.net.client.BoardGameClient
import tools.aqua.bgw.net.client.NetworkLogging
import tools.aqua.bgw.net.common.annotations.GameActionReceiver
import tools.aqua.bgw.net.common.notification.PlayerJoinedNotification
import tools.aqua.bgw.net.common.response.CreateGameResponse
import tools.aqua.bgw.net.common.response.GameActionResponse
import tools.aqua.bgw.net.common.response.JoinGameResponse

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
        // Open game lobby
    }

    /**
     *
     */
    override fun onJoinGameResponse(response: JoinGameResponse) {
        // Wait for game to start
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
