package service

import entity.PlayerInfo
import entity.GameTile

@Suppress("UNUSED_PARAMETER","UNUSED","UndocumentedPublicFunction","UndocumentedPublicClass","EmptyFunctionBlock")
class SetupService(private val rootService: RootService) : AbstractRefreshingService() {

    fun startLocalGame(players: List<PlayerInfo?>?, tilesRotatable: Boolean, aISpeed: Int) {}

    fun startNetworkGame(
        isHostPlayer: Boolean,
        players: List<PlayerInfo?>?,
        tilesRotatable: Boolean,
        tileIDs: List<Int>?,
        aISpeed: Int
    ) {
    }

    private fun createPlayers(playerInfos: List<PlayerInfo>) {}
    private fun initializeDrawPile(): List<GameTile>? {
        return null
    }
}