package view

import tools.aqua.bgw.core.BoardGameApplication
import service.RootService

@Suppress("UNUSED","UndocumentedPublicFunction","UndocumentedPublicClass","EmptyFunctionBlock")

class CableCarApplication : BoardGameApplication("Cable Car") {


    private val rootService = RootService()
    private val gameScene = GameScene(rootService)
    private val endScene =  EndScene(rootService).apply {
        exitButton.onMouseClicked = { exit() }
    }
    private val chooseModeScene = ChooseModeScene(rootService)
    private val lobbyScene = LobbyScene(rootService)

    init {
        this.showGameScene(gameScene)
    }
}