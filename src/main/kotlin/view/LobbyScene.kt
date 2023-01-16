package view

import tools.aqua.bgw.core.MenuScene
import service.RootService
@Suppress("UNUSED_PARAMETER","UNUSED","UndocumentedPublicFunction","UndocumentedPublicClass","EmptyFunctionBlock")



class LobbyScene(private val rootService: RootService) : MenuScene(), Refreshable {

    /**
     * @see view.Refreshable.refreshAfterStartGame
     */
    override fun refreshAfterStartGame() {
    }

    /**
     * @see view.Refreshable.refreshAfterHostGame
     */
    override fun refreshAfterHostGame() {
    }

    /**
     * @see view.Refreshable.refreshAfterJoinGame
     */
    override fun refreshAfterJoinGame(names: List<String>) {
    }
}