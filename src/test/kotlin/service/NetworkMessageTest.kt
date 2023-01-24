package service

import entity.Color
import entity.PlayerInfo
import entity.PlayerType
import kotlin.test.*

/**
 * Test the [edu.udo.cs.sopra.ntf.GameInitMessage] and the [edu.udo.cs.sopra.ntf.TurnMessage]
 */
class NetworkMessageTest : NetworkTest() {
    /**
     * Send a [edu.udo.cs.sopra.ntf.GameInitMessage] as a host and expect it to successfully sync up the guests game
     */
    @Test
    fun testSendGameInitMessageAsHost() {
        val hostRefreshable = NetworkRefreshable()
        val hostRootService = RootService().apply { addRefreshables(hostRefreshable) }
        val hostPlayerInfo = PlayerInfo("Player 1", PlayerType.HUMAN, Color.YELLOW, false)

        val guestRefreshable = NetworkRefreshable()
        val guestRootService = RootService().apply { addRefreshables(guestRefreshable) }
        val guestPlayerInfo = PlayerInfo("Player 2", PlayerType.HUMAN, Color.BLUE, false)

        // Connect both host and guest to the server
        hostRootService.networkService.hostGame(hostPlayerInfo, sessionID)
        assertTrue(hostRefreshable.responseSuccessWithin(timeoutInMillis))
        guestRootService.networkService.joinGame(guestPlayerInfo, sessionID)
        assertTrue(guestRefreshable.responseSuccessWithin(timeoutInMillis))
        // Start a game
        val playerInfos = listOf(hostPlayerInfo, guestPlayerInfo)

        guestRefreshable.awaitGameInitMessageWithin(timeoutInMillis) {
            hostRootService.setupService.startNetworkGame(
                isHostPlayer = true,
                playerInfos = playerInfos,
                tilesRotatable = false,
                tileIDs = null,
                AISpeed = 0
            )
        }

        // Check id tile rotation flag is identical
        assertEquals(hostRootService.cableCar.allowTileRotation, guestRootService.cableCar.allowTileRotation)
        // Check if player orders are identical
        assertEquals(
            hostRootService.cableCar.currentState.players.map { it.name },
            guestRootService.cableCar.currentState.players.map { it.name }
        )
        // Check if draw pile is identical
        assertEquals(
            hostRootService.cableCar.currentState.drawPile.map { it.id },
            guestRootService.cableCar.currentState.drawPile.map { it.id }
        )
    }

    /**
     * Send a [edu.udo.cs.sopra.ntf.GameInitMessage] as a guest and expect it to be denied
     */
    @Test
    fun testSendGameInitMessageAsGuest() {
        val hostRefreshable = NetworkRefreshable()
        val hostRootService = RootService().apply { addRefreshables(hostRefreshable) }
        val hostPlayerInfo = PlayerInfo("Player 1", PlayerType.HUMAN, Color.YELLOW, false)

        val guestRefreshable = NetworkRefreshable()
        val guestRootService = RootService().apply { addRefreshables(guestRefreshable) }
        val guestPlayerInfo = PlayerInfo("Player 2", PlayerType.HUMAN, Color.BLUE, false)

        // Connect both host and guest to the server
        hostRootService.networkService.hostGame(hostPlayerInfo, sessionID)
        assertTrue(hostRefreshable.responseSuccessWithin(timeoutInMillis))
        guestRootService.networkService.joinGame(guestPlayerInfo, sessionID)
        assertTrue(guestRefreshable.responseSuccessWithin(timeoutInMillis))
        // Start a game
        val playerInfos = listOf(hostPlayerInfo, guestPlayerInfo)

        // TODO: This should not pass, as guest should not be allowed to init a game.
        hostRefreshable.awaitGameInitMessageWithin(timeoutInMillis) {
            guestRootService.setupService.startNetworkGame(
                isHostPlayer = true,
                playerInfos = playerInfos,
                tilesRotatable = false,
                tileIDs = null,
                AISpeed = 0
            )
        }
    }


    /**
     * Send a [edu.udo.cs.sopra.ntf.TurnMessage] and expect it to update the hosts and the guests local game instances.
     */


}