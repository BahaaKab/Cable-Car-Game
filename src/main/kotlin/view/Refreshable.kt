package view

@Suppress("UNUSED_PARAMETER","UNUSED","UndocumentedPublicFunction","UndocumentedPublicClass","EmptyFunctionBlock")

interface Refreshable {
    fun refreshAfterEndGame() { }
    fun refreshAfterStartGame() { }
    fun refreshAfterHostGame() { }
    fun refreshAfterJoinGame(names: List<String>) { }
    fun refreshAfterGuestJoined(name: String) { }
    fun refreshAfterGuestLeft(name: String) { }
    fun refreshAfterCalculatePoints() { }
    fun refreshAfterRotateTileLeft() { }
    fun refreshAfterRotateTileRight() { }
    fun refreshAfterUndo() { }
    fun refreshAfterRedo() { }
    fun refreshAfterPlaceTile(posX : Int, posY : Int) { }
    fun refreshAfterDrawTile() { }
    fun refreshAfterGetTurn() { }
    fun refreshAfterNextTurn() { }
}