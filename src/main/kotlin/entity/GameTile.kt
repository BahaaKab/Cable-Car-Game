package entity

/**
 * A game tile
 * Implements [Tile] with eighth [Tile.connectors] and eight directed [Tile.connections]. Although we think
 * of a connection as an undirected edge, calculations can be simplified by saving a connection a <--> b as
 * two directed edges a --> b and b --> a.
 *
 * @param id A unique Identifier, that has to match the scheme of the network card deck. It is given through the order
 * of rows of the tiles.csv deck.
 *
 * @throws IllegalArgumentException If the wrong amount of connections or connectors is set.
 * @constructor Creates a game tile.
 */
class GameTile(val id: Int, connections: List<Int>): Tile(
    List(8) { it }, connections, false, false
) {
    init {
        require(connectors.size == 8)
        require(connections.size == 8)
    }

    /**
     * Increase the previously scored points by one.
     */
    override fun updatePoints(points: Int) = points + 1

    override fun deepCopy() = GameTile(id, connectors.toList())
}