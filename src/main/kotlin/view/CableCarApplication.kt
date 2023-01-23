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
    var lobbyScene = LobbyScene(rootService)
    val connectionScene = ConnectionScene(rootService)
    val connectingScene = ConnectingScene(rootService)


    init {
        rootService.addRefreshables(this, gameScene, endScene, lobbyScene, connectingScene, connectionScene)
        showMenuScene(chooseModeScene)
    }
}