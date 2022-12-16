package entity

/**
 * A power station tile
 * Implements a [Tile] with two [Tile.connectors], no [Tile.connections].
 *
 * @throws IllegalArgumentException If the wrong amount of connections or connectors is set.
 * @constructor Creates a power station tile.
 */
class PowerStationTile(connectors: List<Int>): Tile(connectors, listOf(), false, true) {
    init {
        require(connectors.size == 2)
        require(connections.isEmpty())
    }

    /**
     * Multiply the previously scored points by two.
     */
    override fun updatePoints(points: Int) = points * 2

    override fun deepCopy() = PowerStationTile(connectors.toList())
}