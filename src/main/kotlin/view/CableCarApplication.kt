package view

import tools.aqua.bgw.core.BoardGameApplication
import service.RootService

@Suppress("UNUSED_PARAMETER","UNUSED","UndocumentedPublicFunction","UndocumentedPublicClass","EmptyFunctionBlock")

object CableCarApplication : BoardGameApplication("Cable Car"), Refreshable {


    private val rootService = RootService()
    private val gameScene = GameScene(rootService)
    private val endScene =  EndScene(rootService)
    private val chooseModeScene = ChooseModeScene(rootService)
    private val lobbyScene = LobbyScene(rootService)
     val connectionScene = ConnectionScene(rootService)
     val connectingScene = ConnectingScene(rootService)


    init {
        showMenuScene(connectionScene)
    }
}