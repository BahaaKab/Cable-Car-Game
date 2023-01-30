package view

import entity.Color
import tools.aqua.bgw.net.common.notification.Notification
import tools.aqua.bgw.net.common.response.Response

interface Refreshable {
    fun refreshAfterEndGame() {}
    fun refreshAfterStartGame() {}
    fun refreshAfterNetworkResponse(response: Response) {}
    fun refreshAfterNetworkNotification(notification: Notification) {}
    fun refreshAfterHostGame() {}
    fun refreshAfterJoinGame(names: List<String>) {}
    fun refreshAfterGuestJoined(name: String) {}
    fun refreshAfterGuestLeft(name: String) {}
    fun refreshAfterCalculatePoints() {}
    fun refreshAfterRotateTileLeft() {}
    fun refreshAfterRotateTileRight() {}
    fun refreshAfterUndo(oldState: entity.State) {}
    fun refreshAfterRedo(oldState: entity.State) {}
    fun refreshAfterPlaceTile(posX: Int, posY: Int) {}
    fun refreshAfterPathElementUpdated(x: Int, y: Int, connectionA: Int, connectionB: Int, color: Color) {}
    fun refreshPathAfterEndTile(x: Int, y: Int, connector: Int, color: Color) {}
    fun refreshAfterDrawTile() {}
    fun refreshAfterGetTurn() {}
    fun refreshAfterNextTurn() {}
}