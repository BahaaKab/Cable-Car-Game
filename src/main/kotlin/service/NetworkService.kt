package service

import edu.udo.cs.sopra.ntf.GameInitMessage
import edu.udo.cs.sopra.ntf.GameStateVerificationInfo
import edu.udo.cs.sopra.ntf.TurnMessage
import edu.udo.cs.sopra.ntf.TileInfo
import entity.PlayerInfo

/**
 *
 */
class NetworkService(private val rootService: RootService) : AbstractRefreshingService() {
    /**
     * The sopra server hosting the BGW sessions.
     */
    val HOST = "sopra.cs.tu-dortmund.de:80/bgw-net/connect"

    /**
     * The global secret that is used to verify a connection with the server.
     */
    val SECRET = "cable22"

    /**
     *
     */
    var networkClient: CableCarNetworkClient? = null

    /**
     *
     */
    fun hostGame(player: PlayerInfo, sessionID: String) {
        val client = CableCarNetworkClient(
            this,
            playerName = player.name,
            host = HOST,
            secret = SECRET
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
    fun joinGame(player: PlayerInfo, sessionID: String) {
        val client = CableCarNetworkClient(
            this,
            playerName = player.name,
            host = HOST,
            secret = SECRET
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
        val gameStateVerificationInfo = GameStateVerificationInfo(
            placedTiles = listOf<TileInfo>(), // TODO
            supply = rootService.cableCar.currentState.drawPile.map { it.id },
            playerScores = rootService.cableCar.currentState.players.map { it.score }
        )
        val turnMessage = TurnMessage(posX, posY, fromSupply, rotation, gameStateVerificationInfo)
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
                tileSupply = currentState.drawPile.map { it.toNetworkTile() }
            )
            if (client.isOpen) { client.sendGameActionMessage(gameInitMessage) }
        }
    }
}