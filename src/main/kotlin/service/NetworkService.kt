package service

import entity.PlayerInfo

class NetworkService : AbstractRefreshingService() {
    var networkClient: CableCarNetworkClient? = null
    private val rootService: RootService? = null
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