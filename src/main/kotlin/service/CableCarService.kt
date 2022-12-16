package service


import entity.StationTile
import entity.Tile
import entity.Player
@Suppress("UNUSED_PARAMETER","UNUSED","UndocumentedPublicFunction","UndocumentedPublicClass","EmptyFunctionBlock")
class CableCarService(private val rootService: RootService) : AbstractRefreshingService() {

    fun calculatePoints() {}
    fun nextTurn() {}
    fun endGame() {}
    fun calculateWinners(): List<Player>? {
        return null
    }

    fun updatePaths(tile: Tile?, posX: Int, posY: Int) {}
    private fun updatePath(stationTile: StationTile) {}
}