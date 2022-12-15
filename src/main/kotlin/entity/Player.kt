package entity

/**
 * @property playerType
 * @property color
 * @property name
 * @property score
 */
class Player(val playerType : PlayerType, val color : Color, val name : String,
            val stationTiles : List<StationTile>) {
    var score : Int = 0
}