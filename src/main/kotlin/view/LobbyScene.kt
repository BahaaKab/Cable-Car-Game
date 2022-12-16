package view

import tools.aqua.bgw.core.MenuScene
import service.RootService


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
    override fun refreshAfterJoinGame() {
    }
}