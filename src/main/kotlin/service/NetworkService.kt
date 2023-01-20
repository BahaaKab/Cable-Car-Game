package service

import edu.udo.cs.sopra.ntf.GameInitMessage
import edu.udo.cs.sopra.ntf.GameStateVerificationInfo
import edu.udo.cs.sopra.ntf.TurnMessage
import edu.udo.cs.sopra.ntf.TileInfo
import entity.PlayerInfo

/**
 * Connect with the SoPra-server and send network messages.
 */
class NetworkService(val rootService: RootService) : AbstractRefreshingService() {
    /**
     * The sopra server hosting the BGW sessions
     */
    private val host = "sopra.cs.tu-dortmund.de:80/bgw-net/connect"

    /**
     * The global secret that is used to verify a connection with the server
     */
    private val secret = "cable22"

    /**
     * The Game ID
     */
    private val gameID = "CableCar"

    /**
     * The network client instance
     */
    private lateinit var networkClient: CableCarNetworkClient


    /**
     * Connect to the SoPra-server and create a game lobby.
     *
     * @param player The hosts [PlayerInfo]
     * @param sessionID The ID that other players will need to join the session
     */
    fun hostGame(player: PlayerInfo, sessionID: String) {
        val client = CableCarNetworkClient(
            this,
            playerName = player.name,
            host = host,
            secret = secret
        )

        networkClient = client

        if (client.connect()) {
            client.createGame(
                gameID = gameID,
                sessionID = sessionID,
                greetingMessage = "Welcome to CableCar! You just joined the session '$sessionID'."
            )
        }
    }

    /**
     * Connect to the SoPra-server and join a game.
     *
     * @param player The players [PlayerInfo]
     * @param sessionID The ID of the game lobby, that should be joined
     */
    fun joinGame(player: PlayerInfo, sessionID: String) {
        val client = CableCarNetworkClient(
            this,
            playerName = player.name,
            host = host,
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
     * Disconnect safely from the SoPra-server
     */
    fun disconnect() {
        // Else, if the client is connected, disconnect safely
        if (::networkClient.isInitialized && networkClient.isOpen) {
            networkClient.disconnect()
        }
    }

    /**
     * Send a [TurnMessage] to the other network players.
     *
     * @param posX The x position of the tile to place
     * @param posY The y position of the tile to place
     * @param fromSupply Whether the tile was drawn from the draw pile
     * @param rotation The rotation of the tile
     *
     * @throws IllegalStateException If no [CableCarNetworkClient] was initialized.
     */
    fun sendTurnMessage(posX: Int, posY: Int, fromSupply: Boolean, rotation: Int) {
        // If client is connected, send TurnMessage
        check(::networkClient.isInitialized && networkClient.isOpen)
        val gameStateVerificationInfo = GameStateVerificationInfo(
            placedTiles = rootService.cableCar.currentState.placedTiles,
            supply = rootService.cableCar.currentState.drawPile.map { it.id },
            playerScores = rootService.cableCar.currentState.players.map { it.score }
        )
        val turnMessage = TurnMessage(posX, posY, fromSupply, rotation, gameStateVerificationInfo)
        networkClient.sendGameActionMessage(turnMessage)
    }

    /**
     * Send a [GameInitMessage] to sync the guest players to your game state.
     *
     * @param playerInfos All player infos. The list order defines the turn order.
     *
     * @throws IllegalStateException If no [CableCarNetworkClient] was initialized.
     */
    fun sendGameInitMessage(playerInfos: List<PlayerInfo>) {
        check(::networkClient.isInitialized && networkClient.isOpen)
        // If client is connected, send GameInitMessage
        with (rootService.cableCar) {
            val players = playerInfos.map { it.toNetworkPlayerInfo() }
            val gameInitMessage = GameInitMessage(
                rotationAllowed = allowTileRotation,
                players = players,
                tileSupply = currentState.drawPile.map { it.toNetworkTile() }
            )
            networkClient.sendGameActionMessage(gameInitMessage)
        }
    }
}