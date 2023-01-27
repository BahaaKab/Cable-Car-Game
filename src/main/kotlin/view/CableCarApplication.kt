package view

import service.RootService
import tools.aqua.bgw.core.BoardGameApplication

@Suppress("UNUSED", "UndocumentedPublicFunction", "UndocumentedPublicClass", "EmptyFunctionBlock")

object CableCarApplication : BoardGameApplication("Cable Car"), Refreshable {


    private val rootService = RootService()
    private val endScene = EndScene(rootService).apply {
        exitButton.onMouseClicked = { exit() }
    }
    val chooseModeScene = ChooseModeScene(rootService)
    val localLobbyScene = LobbyScene(rootService)
    val hostLobbyScene = LobbyScene(rootService, true, "", true)
    val guestLobbyScene = LobbyScene(rootService, true, "", false)
    val connectionScene = ConnectionScene(rootService)
    val gameScene = GameScene(rootService)

    val lobbyScenes = listOf(localLobbyScene, hostLobbyScene, guestLobbyScene)


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

