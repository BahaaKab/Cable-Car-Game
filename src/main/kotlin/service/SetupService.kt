package service

import entity.PlayerInfo
import entity.GameTile

@Suppress("UNUSED_PARAMETER","UNUSED")
class SetupService : AbstractRefreshingService() {
    private val rootService: RootService? = null
    fun startLocalGame(players: List<PlayerInfo?>?, tilesRotatable: Boolean, AISpeed: Int) {}

    fun startNetworkGame(
        isHostPlayer: Boolean,
        players: List<PlayerInfo?>?,
        tilesRotatable: Boolean,
        tileIDs: List<Int>?,
        AISpeed: Int
    ) {
    }

    private fun createPlayers(playerInfos: List<PlayerInfo>) {}
    private fun initializeDrawPile(): List<GameTile>? {
        return null
    }
}