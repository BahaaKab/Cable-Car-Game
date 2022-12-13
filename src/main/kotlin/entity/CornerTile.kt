package entity

/**
 * An empty corner tile
 * Implements a [Tile] with no [Tile.connectors] and no [Tile.connections].
 *
 * @throws IllegalArgumentException If the connectors or connections exist.
 * @constructor Creates an empty corner tile.
 */
class CornerTile : Tile() {
    override val isEmpty = true
    override val isEndTile = false

    init {
        require(connectors.isEmpty())
        require(connections.isEmpty())
    }
}