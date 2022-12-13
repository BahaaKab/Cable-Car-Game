package entity

/**
 * The [State] Class is used to save the current state of the game-board.
 *
 * @property drawpile is the current stack of GameTiles. It contains between 0 and 60 GameTiles
 * @property activePlayer is used to indicate the current Player
 */
class State (var drawpile : MutableList<GameTile>, var activePlayer : Player) {

    /**
     * @property board is a two-dimensional array which represents the game-board
     */
    var board: Array<Array<Tile?>> = arrayOf()

}