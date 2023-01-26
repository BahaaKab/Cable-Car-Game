package view

import service.RootService
import tools.aqua.bgw.core.BoardGameApplication

@Suppress("UNUSED", "UndocumentedPublicFunction", "UndocumentedPublicClass", "EmptyFunctionBlock")

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

    override fun refreshAfterEndGame(closeApplication: Boolean) {
        if(closeApplication && !refreshAfterEndGameTriggered) {
            println("Someone left, closing the application.")
            exit()
        }
        refreshAfterEndGameTriggered = true
    }
}

