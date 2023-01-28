package entity

/**
 * A class representing a player.
 *
 * @property playerType
 * @property color
 * @property name
 * @property score
 * @property handTile
 * @property currentTile
 * @property isNetworkPlayer
 */
data class Player(
    val playerType: PlayerType, val color: Color, val name: String,
    val stationTiles: List<StationTile>, val isNetworkPlayer: Boolean = false
) {

    var score: Int = 0
    var handTile: GameTile? = null
    var currentTile: GameTile? = null

    /**
     * Create a player entity from a [PlayerInfo] object.
     *
     * @param playerInfo Information defining a player except from his station tiles.
     * @param stationTiles The players stationTiles.
     */
    constructor(playerInfo: PlayerInfo, stationTiles: List<StationTile>) :
            this(playerInfo.playerType, playerInfo.color, playerInfo.name, stationTiles, playerInfo.isNetworkPlayer)


    /**
     * Function that returns a deep-copy of a [Player]
     * @return deep-copy of [Player]
     */
    fun copy(): Player {
        return Player(playerType, color, name, stationTiles, isNetworkPlayer).also {
            it.score = score
            it.handTile = handTile?.deepCopy()
            it.currentTile = currentTile?.deepCopy()
        }
    }
}