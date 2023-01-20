package service

import entity.Color
import entity.PLAYER_ORDER_COLORS
import entity.PlayerInfo
import entity.PlayerType
import tools.aqua.bgw.net.common.notification.Notification
import tools.aqua.bgw.net.common.notification.PlayerJoinedNotification
import tools.aqua.bgw.net.common.notification.PlayerLeftNotification
import tools.aqua.bgw.net.common.response.*

import view.Refreshable
import kotlin.random.Random
import kotlin.random.nextUInt
import kotlin.test.*


/**
 * A class to handle network [Response] and [Notification]
 */
class NetworkRefreshable() : Refreshable {
    /**
     * A response or null. May be either a [CreateGameResponse] or a [JoinGameResponse]
     */
    var response: Response? = null

    /**
     * A notification or null. May be either a [PlayerJoinedNotification] or a [PlayerLeftNotification]
     */
    var notification : Notification? = null

    /**
     * Set the [NetworkRefreshable.response] property
     *
     * @param response The response
     */
    override fun refreshAfterNetworkResponse(response: Response) {
        this.response = response
    }

    /**
     * Set the [NetworkRefreshable.notification] property
     *
     * @param notification The notification
     */
    override fun refreshAfterNetworkNotification(notification: Notification) {
        this.notification = notification
    }

    /**
     * Whether a connection was successful within a timeout duration.
     * A connection is successful when the response was returning either a [CreateGameResponseStatus.SUCCESS] or a
     * [JoinGameResponseStatus.SUCCESS].
     *
     * @param timeoutInMillis The timeout duration in milliseconds
     *
     * @return Whether the connection was successful
     */
    fun responseSuccessWithin(timeoutInMillis: Int) : Boolean {
        responseWithinOrNull(timeoutInMillis)
        return (response is JoinGameResponse && (response as JoinGameResponse).status == JoinGameResponseStatus.SUCCESS) ||
                (response is CreateGameResponse && (response as CreateGameResponse).status == CreateGameResponseStatus.SUCCESS)
    }

    /**
     * Return a notification with in a timeout duration or null otherwise.
     *
     * @param timeoutInMillis The timeout duration in milliseconds
     *
     * @return A notification or null, when the timeout was exceeded
     */
    fun notificationWithinOrNull(timeoutInMillis: Int) : Notification? {
        var counter = timeoutInMillis
        while(notification == null) {
            Thread.sleep(5)
            if (counter <= 0) {
                return null
            }
            counter -= 5
        }
        return notification
    }

    /**
     * Return a response with in a timeout duration or null otherwise.
     *
     * @param timeoutInMillis The timeout duration in milliseconds
     *
     * @return A response or null, when the timeout was exceeded
     */
    private fun responseWithinOrNull(timeoutInMillis: Int) : Response? {
        var counter = timeoutInMillis
        while(response == null) {
            Thread.sleep(5)
            if (counter <= 0) {
                return null
            }
            counter -= 5
        }
        return response
    }
}

/**
 * Tests for the [NetworkService] and the [CableCarNetworkClient].
 */
class NetworkTest {
    /**
     * A timeout default. Waiting for more than 5 seconds for a response or notification should be considered as
     * a fail, which is most likely caused by the server.
     */
    private val timeoutInMillis = 5000

    /**
     * The base session ID. For more infos see [setUniqueSessionId].
     */
    private val coreSessionID = "CableCarSession"

    /**
     * A random starting ID. For more infos see [setUniqueSessionId].
     */
    private var sessionIDCounter = Random.nextUInt()

    /**
     * The actual session ID. For more infos see [setUniqueSessionId].
     */
    private var sessionID = ""


    /**
     * Generate a unique session ID for each test.
     * As the server keeps a session alive for a few seconds after the last player left it, the tests might run in
     * unexpected errors, when they use the same session ID, as the server might return a
     * [CreateGameResponseStatus.SESSION_WITH_ID_ALREADY_EXISTS] status. Therefore this method generates unique session
     * IDs for each test. The [sessionIDCounter] is initialized randomly, when the class is created. This method will
     * be called automatically before each test and will set the [sessionID] with a incremented [sessionIDCounter].
     */
    @BeforeTest
    fun setUniqueSessionId() {
        sessionID = "${coreSessionID}_${sessionIDCounter ++}"
    }

    /**
     * Connect to the server as host and expect a [CreateGameResponseStatus.SUCCESS] status
     */
    @Test
    fun testHostGameSuccess() {
        val hostRefreshable = NetworkRefreshable()
        val hostRootService = RootService().apply { addRefreshables(hostRefreshable) }
        val hostPlayerInfo = PlayerInfo("Player 1", PlayerType.HUMAN, Color.YELLOW, false)

        hostRootService.networkService.hostGame(hostPlayerInfo, sessionID)
        assertTrue(hostRefreshable.responseSuccessWithin(timeoutInMillis))
    }

    /**
     * Connect twice as host with the same [sessionID] and expect a
     * [CreateGameResponseStatus.ALREADY_ASSOCIATED_WITH_GAME] status
     */
    @Test
    fun testHostSessionAlreadyExists() {
        val hostRefreshable = NetworkRefreshable()
        val hostRootService = RootService().apply { addRefreshables(hostRefreshable) }
        val hostPlayerInfo = PlayerInfo("Player 1", PlayerType.HUMAN, Color.YELLOW, false)

        val anotherHostRefreshable = NetworkRefreshable()
        val anotherHostRootService = RootService().apply { addRefreshables(hostRefreshable) }
        val anotherHostPlayerInfo = PlayerInfo("Player 2", PlayerType.HUMAN, Color.BLUE, false)

        hostRootService.networkService.hostGame(hostPlayerInfo, sessionID)
        assertTrue(hostRefreshable.responseSuccessWithin(timeoutInMillis))
        anotherHostRootService.networkService.hostGame(anotherHostPlayerInfo, sessionID)
        assertFalse(anotherHostRefreshable.responseSuccessWithin(timeoutInMillis))
    }

    /**
     * Connect to the server as guest and expect a [JoinGameResponseStatus.SUCCESS] status
     */
    @Test
    fun testJoinGameSuccess() {
        val hostRefreshable = NetworkRefreshable()
        val hostRootService = RootService().apply { addRefreshables(hostRefreshable) }
        val hostPlayerInfo = PlayerInfo("Player 1", PlayerType.HUMAN, Color.YELLOW, false)

        val guestRefreshable = NetworkRefreshable()
        val guestRootService = RootService().apply { addRefreshables(guestRefreshable) }
        val guestPlayerInfo = PlayerInfo("Player 2", PlayerType.HUMAN, Color.BLUE, true)

        hostRootService.networkService.hostGame(hostPlayerInfo, sessionID)
        assertTrue(hostRefreshable.responseSuccessWithin(timeoutInMillis))
        guestRootService.networkService.joinGame(guestPlayerInfo, sessionID)
        assertTrue(guestRefreshable.responseSuccessWithin(timeoutInMillis))
        val gameJoinedResponse = (guestRefreshable.response as JoinGameResponse)
        assertEquals(hostPlayerInfo.name, gameJoinedResponse.opponents.first())
    }

    /**
     * Connect to the server as guest with a bad [sessionID] and expect a [JoinGameResponseStatus.INVALID_SESSION_ID]
     * status
     */
    @Test
    fun testJoinGameInvalidSessionId() {
        val invalidSessionID = "INVALID_$coreSessionID"
        val guestRefreshable = NetworkRefreshable()
        val guestRootService = RootService().apply { addRefreshables(guestRefreshable) }
        val guestPlayerInfo = PlayerInfo("Player 2", PlayerType.HUMAN, Color.BLUE, true)

        guestRootService.networkService.joinGame(guestPlayerInfo, invalidSessionID)
        assertFalse(guestRefreshable.responseSuccessWithin(timeoutInMillis))
    }

    /**
     * Connect two guests with the same name to the server and expect a
     * [JoinGameResponseStatus.PLAYER_NAME_ALREADY_TAKEN] status
     */
    @Test
    fun testJoinGamePlayerNameAlreadyTaken() {
        val playerName = "Player"
        val hostRefreshable = NetworkRefreshable()
        val hostRootService = RootService().apply { addRefreshables(hostRefreshable) }
        val hostPlayerInfo = PlayerInfo(playerName, PlayerType.HUMAN, Color.YELLOW, false)

        val guestRefreshable = NetworkRefreshable()
        val guestRootService = RootService().apply { addRefreshables(guestRefreshable) }
        val guestPlayerInfo = PlayerInfo(playerName, PlayerType.HUMAN, Color.BLUE, true)

        hostRootService.networkService.hostGame(hostPlayerInfo, sessionID)
        assertTrue(hostRefreshable.responseSuccessWithin(timeoutInMillis))
        guestRootService.networkService.joinGame(guestPlayerInfo, sessionID)
        assertFalse(guestRefreshable.responseSuccessWithin(timeoutInMillis))
    }

    /**
     * Sequentially connect and then disconnect five guest players and expect all players, that are in the game lobby
     * to receive notifications.
     */
    @Test
    fun testNotifications() {
        val networkPlayerInfos = PLAYER_ORDER_COLORS.mapIndexed { i, color ->
            // The first player should be the host
            val isNetworkPlayer = i == 0
            PlayerInfo("Player $i", PlayerType.HUMAN, color, isNetworkPlayer )
        }

        val hostRefreshable = NetworkRefreshable()
        val hostRootService = RootService().apply { addRefreshables(hostRefreshable) }
        val hostPlayerInfo = networkPlayerInfos.first()
        // Start the game session
        hostRootService.networkService.hostGame(hostPlayerInfo, sessionID)
        assertTrue(hostRefreshable.responseSuccessWithin(timeoutInMillis))

        val guestPlayerInfos = networkPlayerInfos.subList(1, networkPlayerInfos.size)
        val guestRefreshables = List(guestPlayerInfos.size) { NetworkRefreshable() }
        val guestRootServices = List(guestPlayerInfos.size) { i ->
            RootService().apply { addRefreshables(guestRefreshables[i]) }
        }

        for (i in guestPlayerInfos.indices) {
            val guestRootService = guestRootServices[i]
            val guestPlayerInfo = guestPlayerInfos[i]
            val guestRefreshable = guestRefreshables[i]

            val refreshablesOfConnectedPlayers = mutableListOf(hostRefreshable)
            refreshablesOfConnectedPlayers.addAll(guestRefreshables.subList(0, i))
            // Clear the notifications
            refreshablesOfConnectedPlayers.forEach { it.notification = null }
            // Join player
            guestRootService.networkService.joinGame(guestPlayerInfo, sessionID)
            assertTrue(guestRefreshable.responseSuccessWithin(timeoutInMillis))
            // Check if the other players got notified
            refreshablesOfConnectedPlayers.forEach {
                val notification = it.notificationWithinOrNull(timeoutInMillis) as PlayerJoinedNotification?
                assertNotNull(notification)
                assertEquals(guestPlayerInfo.name, notification.sender)
            }
        }

        for (i in guestPlayerInfos.indices.reversed()) {
            val guestRootService = guestRootServices[i]
            val guestPlayerInfo = guestPlayerInfos[i]

            val refreshablesOfConnectedPlayers = mutableListOf(hostRefreshable)
            refreshablesOfConnectedPlayers.addAll(guestRefreshables.subList(0, i))
            // Clear the notifications
            refreshablesOfConnectedPlayers.forEach { it.notification = null }
            // Disconnect player
            guestRootService.networkService.disconnect()
            // Check if the other players got notified
            refreshablesOfConnectedPlayers.forEach {
                val notification = it.notificationWithinOrNull(timeoutInMillis) as PlayerLeftNotification?
                assertNotNull(notification)
                assertEquals(guestPlayerInfo.name, notification.sender)
            }
        }
    }
}

