package entity

/**
 * A game tile
 * Implements [Tile] with eighth [Tile.connectors] and eight directed [Tile.connections]. Although we think
 * of a connection as an undirected edge, calculations can be simplified by saving a connection a <--> b as
 * two directed edges a --> b and b --> a.
 *
 * @throws IllegalArgumentException If the wrong amount of connections or connectors is set.
 * @constructor Creates a game tile.
 */
class GameTile(connectors: List<Position>, connections: List<Position>): Tile(connectors, connections) {
    override val isEmpty = false
    override val isEndTile = false

    init {
        require(connectors.size == 8)
        require(connections.size == 8)
    }

    /**
     * Increase the previously scored points by one.
     */
    override fun updatePoints(points: Int) = points + 1
}