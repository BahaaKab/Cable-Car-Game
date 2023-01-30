package service

import kotlin.test.*
import view.Refreshable

/**
 * [Refreshable] implementation that refreshes nothing, but remembersif a refresh method has been called.
 */
class RefreshableTest : Refreshable {
    private var refreshAfterEndGameCalled = false
    private var refreshAfterStartGameCalled = false
    private var refreshAfterHostGameCalled = false
    private var refreshAfterJoinGameCalled = false
    private var refreshAfterGuestJoinedCalled = false
    private var refreshAfterGuestLeftCalled = false
    private var refreshAfterCalculatePointsCalled = false
    private var refreshAfterRotateTileLeftCalled = false
    private var refreshAfterRotateTileRightCalled = false
    private var refreshAfterUndoCalled = false
    private var refreshAfterRedoCalled = false
    private var refreshAfterPlaceTileCalled = false
    private var refreshAfterDrawTileCalled = false
    private var refreshAfterGetTurnCalled = false
    private var refreshAfterNextTurnCalled = false

    override fun refreshAfterEndGame() {
        refreshAfterEndGameCalled = true
    }

    override fun refreshAfterStartGame() {
        refreshAfterStartGameCalled = true
    }

    override fun refreshAfterHostGame() {
        refreshAfterHostGameCalled = true
    }

    override fun refreshAfterJoinGame(names: List<String>) {
        refreshAfterJoinGameCalled = true
    }

    override fun refreshAfterGuestJoined(name: String) {
        refreshAfterGuestJoinedCalled = true
    }

    override fun refreshAfterGuestLeft(name: String) {
        refreshAfterGuestLeftCalled = true
    }

    override fun refreshAfterCalculatePoints() {
        refreshAfterCalculatePointsCalled = true
    }

    override fun refreshAfterRotateTileLeft() {
        refreshAfterRotateTileLeftCalled = true
    }

    override fun refreshAfterRotateTileRight() {
        refreshAfterRotateTileRightCalled = true
    }

    override fun refreshAfterUndo(oldState: entity.State) {
        refreshAfterUndoCalled = true
    }

    override fun refreshAfterRedo(oldState: entity.State) {
        refreshAfterRedoCalled = true
    }

    override fun refreshAfterPlaceTile(posX: Int, posY: Int) {
        refreshAfterPlaceTileCalled = true
    }

    override fun refreshAfterDrawTile() {
        refreshAfterDrawTileCalled = true
    }

    override fun refreshAfterGetTurn() {
        refreshAfterGetTurnCalled = true
    }

    override fun refreshAfterNextTurn() {
        refreshAfterNextTurnCalled = true
    }

    /**
     * Checks if the refreshable has been called
     *
     * The refreshable isn't called at the beginning
     * So we call it and check if that was successful
     */
    @Test
    fun testRefreshables() {
        val oldState = entity.State(
            drawPile = mutableListOf(),
            activePlayer = entity.Player(
                playerType = entity.PlayerType.HUMAN, color = entity.Color.BLACK,
                name = "Player", stationTiles = mutableListOf(), isNetworkPlayer = false
            ),
            board = arrayOf(arrayOf()),
            players = mutableListOf()
        )
        assertFalse(refreshAfterEndGameCalled)
        assertFalse(refreshAfterStartGameCalled)
        assertFalse(refreshAfterHostGameCalled)
        assertFalse(refreshAfterJoinGameCalled)
        assertFalse(refreshAfterCalculatePointsCalled)
        assertFalse(refreshAfterRotateTileLeftCalled)
        assertFalse(refreshAfterRotateTileRightCalled)
        assertFalse(refreshAfterUndoCalled)
        assertFalse(refreshAfterRedoCalled)
        assertFalse(refreshAfterPlaceTileCalled)
        assertFalse(refreshAfterDrawTileCalled)
        assertFalse(refreshAfterGetTurnCalled)
        assertFalse(refreshAfterNextTurnCalled)
        refreshAfterEndGame()
        refreshAfterStartGame()
        refreshAfterHostGame()
        refreshAfterJoinGame(listOf<String>())
        refreshAfterCalculatePoints()
        refreshAfterRotateTileLeft()
        refreshAfterRotateTileRight()
        refreshAfterUndo(oldState)
        refreshAfterRedo(oldState)
        refreshAfterPlaceTile(10, 10)
        refreshAfterDrawTile()
        refreshAfterGetTurn()
        refreshAfterNextTurn()
        assertTrue(refreshAfterEndGameCalled)
        assertTrue(refreshAfterStartGameCalled)
        assertTrue(refreshAfterHostGameCalled)
        assertTrue(refreshAfterJoinGameCalled)
        assertTrue(refreshAfterCalculatePointsCalled)
        assertTrue(refreshAfterRotateTileLeftCalled)
        assertTrue(refreshAfterRotateTileRightCalled)
        assertTrue(refreshAfterUndoCalled)
        assertTrue(refreshAfterRedoCalled)
        assertTrue(refreshAfterPlaceTileCalled)
        assertTrue(refreshAfterDrawTileCalled)
        assertTrue(refreshAfterGetTurnCalled)
        assertTrue(refreshAfterNextTurnCalled)
    }
}