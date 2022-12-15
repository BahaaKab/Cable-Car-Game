package service

import entity.StationTile
import entity.Tile
import entity.Player
class CableCarService : AbstractRefreshingService() {
    private val rootService: RootService? = null
    fun calculatePoints() {}
    fun nextTurn() {}
    fun endGame() {}
    fun calculateWinners(): List<Player>? {
        return null
    }

    fun updatePaths(tile: Tile?, posX: Int, posY: Int) {}
    private fun updatePath(stationTile: StationTile) {}
}