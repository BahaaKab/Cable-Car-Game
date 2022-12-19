package view

import tools.aqua.bgw.core.BoardGameApplication
import service.RootService

@Suppress("UNUSED_PARAMETER","UNUSED","UndocumentedPublicFunction","UndocumentedPublicClass","EmptyFunctionBlock")

class CableCarApplication : BoardGameApplication("Cable Car") {

    private val rootService = RootService()
    private val gameScene = GameScene(rootService)
    private val endScene =  EndScene(rootService)
    private val chooseModeScene = ChooseModeScene(rootService)
    private val lobbyScene = LobbyScene(rootService)

    init {
        this.showMenuScene(chooseModeScene)
        //this.showGameScene(gameScene)
    }
}