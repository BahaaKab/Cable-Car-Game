package view

import tools.aqua.bgw.core.BoardGameApplication
import service.RootService

@Suppress("UNUSED", "UndocumentedPublicFunction", "UndocumentedPublicClass", "EmptyFunctionBlock")

object CableCarApplication : BoardGameApplication("Cable Car"), Refreshable {


    private val rootService = RootService()
    private val gameScene = GameScene(rootService)
    private val endScene = EndScene(rootService).apply {
        exitButton.onMouseClicked = { exit() }
    }
    private val chooseModeScene = ChooseModeScene(rootService)
    val lobbyScene = LobbyScene(rootService)
    val connectionScene = ConnectionScene(rootService)
    val connectingScene = ConnectingScene(rootService)


    init {
        rootService.addRefreshables(this, gameScene, endScene, lobbyScene, connectingScene, connectionScene)
        showMenuScene(chooseModeScene)
    }
}