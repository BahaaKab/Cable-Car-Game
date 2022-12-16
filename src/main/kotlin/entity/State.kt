package entity

/**
 * The [State] Class is used to save the current state of the game-board.
 *
 * @property drawPile is the current stack of GameTiles. It contains between 0 and 60 GameTiles
 * @property activePlayer is used to indicate the current Player
 * @property board is a two-dimensional array which represents the game-board
 * @property players: Is a list that contains every Player.
 */
class State (var drawPile : MutableList<GameTile>,
             var activePlayer : Player,
             val board: Array<Array<Tile?>> = arrayOf(),
             val players: List<Player>)
