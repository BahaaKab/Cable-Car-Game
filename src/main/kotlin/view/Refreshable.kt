package view

interface Refreshable {
    fun refreshAfterEndGame() { }
    fun refreshAfterStartGame() { }
    fun refreshAfterHostGame() { }
    fun refreshAfterJoinGame() { }
    fun refreshAfterCalculatePoints() { }
    fun refreshAfterRotateTileLeft() { }
    fun refreshAfterRotateTileRight() { }
    fun refreshAfterUndo() { }
    fun refreshAfterRedo() { }
    fun refreshAfterPlaceTile() { }
    fun refreshAfterDrawTile() { }
    fun refreshAfterGetTurn() { }
    fun refreshAfterNextTurn() { }
}