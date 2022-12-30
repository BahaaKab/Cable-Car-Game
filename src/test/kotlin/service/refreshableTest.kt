package service

import kotlin.test.*
import view.Refreshable

/**
 * [Refreshable] implementation that refreses nothing, but remembers
 * if a refresh method has been called (since last [reset])
 * **/
class refreshableTest : Refreshable {

    var refreshAfterEndGameCalled : Boolean = false
        private set

    var refreshAfterStartGameCalled : Boolean = false
        private set

    var refreshAfterHostGameCalled : Boolean = false
        private set

    var refreshAfterJoinGameCalled : Boolean = false
        private set

    var refreshAfterCalculatePointsCalled : Boolean = false
        private set

    var refreshAfterRotateTileLeftCalled : Boolean = false
        private set

    var refreshAfterRotateTileRightCalled : Boolean = false
        private set

    var refreshAfterUndoCalled : Boolean = false
        private set

    var refreshAfterRedoCalled : Boolean = false
        private set

    var refreshAfterPlaceTileCalled : Boolean = false
        private set

    var refreshAfterDrawTileCalled : Boolean = false
        private set

    var refreshAfterGetTurnCalled : Boolean = false
        private set

    var refreshAfterNextTurnCalled : Boolean = false
        private set

    /**
     * Resets all *Called properties
     * **/
    fun reset(){
        refreshAfterEndGameCalled = false
        refreshAfterStartGameCalled = false
        refreshAfterHostGameCalled = false
        refreshAfterJoinGameCalled = false
        refreshAfterCalculatePointsCalled = false
        refreshAfterRotateTileLeftCalled = false
        refreshAfterRotateTileRightCalled = false
        refreshAfterUndoCalled = false
        refreshAfterRedoCalled = false
        refreshAfterPlaceTileCalled = false
        refreshAfterDrawTileCalled = false
        refreshAfterGetTurnCalled = false
        refreshAfterNextTurnCalled = false
    }

    override fun refreshAfterEndGame() { refreshAfterEndGameCalled = true }
    override fun refreshAfterStartGame() { refreshAfterStartGameCalled = true }
    override fun refreshAfterHostGame() { refreshAfterHostGameCalled = true }
    override fun refreshAfterJoinGame() { refreshAfterJoinGameCalled = true }
    override fun refreshAfterCalculatePoints() { refreshAfterCalculatePointsCalled = true }
    override fun refreshAfterRotateTileLeft() { refreshAfterRotateTileLeftCalled = true }
    override fun refreshAfterRotateTileRight() { refreshAfterRotateTileRightCalled = true }
    override fun refreshAfterUndo() { refreshAfterUndoCalled = true }
    override fun refreshAfterRedo() { refreshAfterRedoCalled = true }
    override fun refreshAfterPlaceTile() { refreshAfterPlaceTileCalled = true }
    override fun refreshAfterDrawTile() { refreshAfterDrawTileCalled = true }
    override fun refreshAfterGetTurn() { refreshAfterGetTurnCalled = true }
    override fun refreshAfterNextTurn() { refreshAfterNextTurnCalled = true }

    @Test
    fun testRefreshables() {
        assertFalse(refreshAfterEndGameCalled, "false")
        refreshAfterEndGame()
        assertTrue(refreshAfterEndGameCalled, "true")
    }

}