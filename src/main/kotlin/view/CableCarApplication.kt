package view

import tools.aqua.bgw.core.BoardGameApplication
import service.RootService
import java.io.File
import java.io.FileNotFoundException

@Suppress("UNUSED_PARAMETER","UNUSED","UndocumentedPublicFunction","UndocumentedPublicClass","EmptyFunctionBlock")

class CableCarApplication : BoardGameApplication("Cable Car") {


    private val rootService = RootService()
    private val gameScene = GameScene(rootService)
    private val endScene =  EndScene(rootService)
    private val chooseModeScene = ChooseModeScene(rootService)
    private val lobbyScene = LobbyScene(rootService)

    init {
        //this.showGameScene(gameScene)
        val uri = CableCarApplication::class.java.getResource("'Arial'")?.toURI()
            ?: throw FileNotFoundException()
        loadFont(File(uri))
        this.showMenuScene(chooseModeScene)
    }
}