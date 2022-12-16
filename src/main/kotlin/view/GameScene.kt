package view

import tools.aqua.bgw.core.BoardGameScene
import service.RootService
import tools.aqua.bgw.components.uicomponents.Label
import tools.aqua.bgw.util.Font
import tools.aqua.bgw.visual.ColorVisual

@Suppress("UNUSED_PARAMETER","UNUSED")

class GameScene(private val rootService: RootService) : BoardGameScene(), Refreshable {

    private val gameLabel = Label(
        width = 500,
        height = 500,
        posX = 0,
        posY = 0,
        text = "Go CableCar!",
        font = Font(size = 20)
    )
    init {
        background = ColorVisual(108, 168, 59)
        addComponents(gameLabel)
    }

    /**
     * @see view.Refreshable.refreshAfterEndGame
     */
    override fun refreshAfterEndGame() {
    }

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

    /**
     * @see view.Refreshable.refreshAfterCalculatePoints
     */
    override fun refreshAfterCalculatePoints() {
    }

    /**
     * @see view.Refreshable.refreshAfterRotateTileLeft
     */
    override fun refreshAfterRotateTileLeft() {
    }

    /**
     * @see view.Refreshable.refreshAfterRotateTileRight
     */
    override fun refreshAfterRotateTileRight() {
    }

    /**
     * @see view.Refreshable.refreshAfterUndo
     */
    override fun refreshAfterUndo() {
    }

    /**
     * @see view.Refreshable.refreshAfterRedo
     */
    override fun refreshAfterRedo() {
    }

    /**
     * @see view.Refreshable.refreshAfterPlaceTile
     */
    override fun refreshAfterPlaceTile() {
    }

    /**
     * @see view.Refreshable.refreshAfterDrawTile
     */
    override fun refreshAfterDrawTile() {
    }

    /**
     * @see view.Refreshable.refreshAfterGetTurn
     */
    override fun refreshAfterGetTurn() {
    }

    /**
     * @see view.Refreshable.refreshAfterNextTurn
     */
    override fun refreshAfterNextTurn() {
    }
}