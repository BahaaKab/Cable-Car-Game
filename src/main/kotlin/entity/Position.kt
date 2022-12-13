package entity

/** The enum Position represents the visual location of a ConnectionPoint on a Tile.*/
enum class Position(val value : Int){
    TOP_LEFT(0),
    TOP_RIGHT(1),
    RIGHT_TOP(2),
    RIGHT_BOT(3),
    BOT_RIGHT(4),
    BOT_LEFT(5),
    LEFT_BOT(6),
    LEFT_TOP(7);
}