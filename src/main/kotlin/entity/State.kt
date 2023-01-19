package entity

import edu.udo.cs.sopra.ntf.TileInfo

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
             val players: List<Player>) {

    /**
     * Tiles that were already placed
     */
    val placedTiles: MutableList<TileInfo> = mutableListOf()

    /**
     * Deep copy a state
     *
     * @returns The copied state
     */
    fun deepCopy() : State {
        val copiedDrawPile = drawPile.map { it.deepCopy() }.toMutableList()
        val copiedBoard = board.map { column -> column.map { it?.deepCopy() }.toTypedArray() }.toTypedArray()
        val copiedPlayers = players.map { it.deepCopy() }
        val newActivePlayer = copiedPlayers[players.indexOf(activePlayer)]
        return State(copiedDrawPile, newActivePlayer, copiedBoard, copiedPlayers)
    }
}
