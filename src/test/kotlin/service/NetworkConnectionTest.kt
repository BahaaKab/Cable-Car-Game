package service

import entity.Color
import entity.PLAYER_ORDER_COLORS
import entity.PlayerInfo
import entity.PlayerType
import tools.aqua.bgw.net.common.notification.PlayerJoinedNotification
import tools.aqua.bgw.net.common.notification.PlayerLeftNotification
import tools.aqua.bgw.net.common.response.*

import kotlin.test.*


/**
 * Tests for the [NetworkService] and the [CableCarNetworkClient].
 */
class NetworkConnectionTest: NetworkTest() {
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
        val guestPlayerInfo = PlayerInfo("Player 2", PlayerType.HUMAN, Color.BLUE, false)

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
        val guestPlayerInfo = PlayerInfo("Player 2", PlayerType.HUMAN, Color.BLUE, false)

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
        val guestPlayerInfo = PlayerInfo(playerName, PlayerType.HUMAN, Color.BLUE, false)

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

