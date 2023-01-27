package view

import service.RootService
import tools.aqua.bgw.core.BoardGameApplication

/** This class administrates all Scenes of the Application.
 *  It also managed how to close the network-connection in case of closing the application-window.
 *
 *  @property rootService the connection to the service- and data-layer of the game.
 *  @property gameScene the scene in which the players play the game
 *  @property endScene the scene shown at the regular end of each game with i.e. scoreboard
 *  @property chooseModeScene a scene in which the player choose to play local or over a network
 *  @property localLobbyScene this scene shows the lobby in case the players want to play in hot-seat-mode
 *  @property hostLobbyScene this scene shows the lobby of a host-player in network-games.
 *  @property guestLobbyScene this scene shows the scene of a connected player in a network-game
 *  @property connectionScene the scene shown when the player wants to create or connect (to) a network-game*/
object CableCarApplication : BoardGameApplication("Cable Car"), Refreshable {


    private val rootService = RootService()
    private val gameScene = GameScene(rootService)
    private val endScene = EndScene(rootService).apply {
        exitButton.onMouseClicked = { exit() }
    }
    val chooseModeScene = ChooseModeScene(rootService)
    val localLobbyScene = LobbyScene(rootService)
    val hostLobbyScene = LobbyScene(rootService, true, "", true)
    val guestLobbyScene = LobbyScene(rootService, true, "", false)
    val connectionScene = ConnectionScene(rootService)

    val lobbyScenes = listOf(localLobbyScene, hostLobbyScene, guestLobbyScene)

    private var refreshAfterEndGameTriggered = false


    init {
        rootService.addRefreshables(
            this,
            gameScene,
            endScene,
            localLobbyScene,
            hostLobbyScene,
            guestLobbyScene,
            connectionScene
        )
        onWindowClosed = {
            rootService.networkService.disconnect()
        }
        showGameScene(gameScene)
        showMenuScene(chooseModeScene)
    }

}

