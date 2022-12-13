package entity

/**
 * A station tile
 * Implements a [Tile] with two [Tile.connectors] and no [Tile.connections]. The connectors have to be on the same side
 * of the tile.
 *
 * @property stationPosition The position where a cable car is set at the start of the game.
 *
 * @throws IllegalArgumentException If the wrong amount of connections or connectors is set or if no [stationPosition]
 * is found.
 * @constructor Creates a station tile.
 */
class StationTile(connectors: List<Position>): Tile(connectors) {
    override val isEmpty = false
    override val isEndTile = true
    val stationPosition: Position

    init {
        require(connectors.size == 2)
        require(connections.isEmpty())
        // If the connectors were set correctly, the start position is always the even of the two (either 0, 2, 4 or 6)
        stationPosition = requireNotNull(
            connectors.find { it.value % 2 == 0 }
        )
    }
}