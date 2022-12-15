package entity

/**
 * @property playerType
 * @property color
 * @property name
 * @property score
 * @property handTile
 * @property currentTile
 */
data class Player(val playerType : PlayerType, val color : Color, val name : String,
            val stationTiles : List<StationTile>) {

    var score : Int = 0
    var handTile : GameTile? = null
    var currentTile : GameTile? = null

    /**
     * Function that returns a deep-copy of a [Player]
     * @return deep-copy of [Player]
     */
    fun deepCopy() : Player {
        val stationTiles : List<StationTile> = this.stationTiles.map {
           return it.deepCopy()
        }
        return Player(playerType, color, name, stationTiles).also {
            it.score = score
            it.handTile = handTile.deepCopy()
            it.currentTile = currentTile.deepCopy()
        }
    }
}