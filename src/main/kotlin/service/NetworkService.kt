package service

import GameInitMessage
import TurnMessage
import entity.PlayerInfo


val SECRET = "cable22"

/**
 *
 */
class NetworkService(private val rootService: RootService) : AbstractRefreshingService() {
    var networkClient: CableCarNetworkClient? = null

    /**
     *
     */
    fun hostGame(secret: String, player: PlayerInfo, sessionID: String) {
        val client = CableCarNetworkClient(
            this,
            playerName = player.name,
            host = "localhost", // TODO: pass from somewhere or is this fix?
            secret = secret
        )

        networkClient = client

        if (client.connect()) {
            client.createGame(
                gameID = "CableCar",  //TODO: pass from somewhere?
                sessionID = sessionID,
                greetingMessage = "Welcome to CableCar! You just joined the session '$sessionID'."
            )
        }
    }

    /**
     *
     */
    fun joinGame(secret: String, player: PlayerInfo, sessionID: String) {
        val client = CableCarNetworkClient(
            this,
            playerName = player.name,
            host = "localhost", // TODO: pass from somewhere or is this fix?
            secret = secret
        )

        networkClient = client

        if (client.connect()) {
            client.joinGame(
                sessionID = sessionID,
                greetingMessage = "Hi, I'm ${player.name}!"
            )
        }
    }

    /**
     *
     */
    fun disconnect() {
        // If no network client exists, return
        val client = networkClient ?: return
        // Else, if the client is connected, disconnect safely
        if (client.isOpen) { client.disconnect() }
    }

    /**
     *
     */
    fun sendTurnMessage(posX: Int, posY: Int, fromSupply: Boolean, rotation: Int) {
        // If no network client exists, throw exception
        val client = checkNotNull(networkClient)
        // Else, if client is connected, send TurnMessage
        val turnMessage = TurnMessage(posX, posY, fromSupply, rotation)
        if (client.isOpen) { client.sendGameActionMessage(turnMessage) }
    }

    /**
     *
     */
    fun sendGameInitMessage(playerInfos: List<PlayerInfo>) {
        // If no network client exists, throw exception
        val client = checkNotNull(networkClient)
        // Else, if client is connected, send GameInitMessage
        with (checkNotNull(rootService.cableCar)) {
            val players = playerInfos.map { it.toNetworkPlayerInfo() }
            val gameInitMessage = GameInitMessage(
                rotationAllowed = allowTileRotation,
                players = players,
                tileSupply = currentState.drawPile.map { it.id }
            )
            if (client.isOpen) { client.sendGameActionMessage(gameInitMessage) }
        }
    }

    /* ## Waiting for NetworkMessage
    fun validateTiles(message: NetworkMessage?): Boolean {
        return false
    }
    */
}