package service

import entity.PlayerInfo
@Suppress("UNUSED_PARAMETER","UNUSED","UndocumentedPublicFunction","UndocumentedPublicClass","EmptyFunctionBlock")

class NetworkService(private val rootService: RootService) : AbstractRefreshingService() {
    var networkClient: CableCarNetworkClient? = null
    fun hostGame(secret: String?, player: PlayerInfo?, sessionID: String?) {
    }

    fun joinGame(secret: String?, player: PlayerInfo?, sessionID: String?) {
    }

    fun disconnect() {
    }

    fun sendTurnMessage(fromSupply: Boolean, posX: Int, posY: Int) {
    }

    fun sendGameInitMessage() {
    }

    /* ## Waiting for NetworkMessage
    fun validateTiles(message: NetworkMessage?): Boolean {
        return false
    }
    */
}