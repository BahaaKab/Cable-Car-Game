package entity

/**
 * An abstract base Tile
 *
 * @property connectors The connectors that exist on the tile
 * @property connections The connections between two connectors, where the "from" connector is represented through the
 * index and the "to" connector is represented through the value at index.
 * @property isEmpty Whether the tile has no connectors
 * @property isEndTile Whether the tile has no connections and therefore is marks one end of a track.
 *
 * @constructor Creates an abstract base tile.
 */
open class Tile(
    val connectors: List<Int>,
    var connections: List<Int>,
    val isEmpty: Boolean,
    val isEndTile: Boolean
) {

    val OUTER_TILE_CONNECTIONS = arrayOf(5, 4, 7, 6, 1, 0, 3, 2)

    /**
     * Update points based on the card rule.
     *
     * @param points Previously calculated points.
     *
     * @return The points scored after applying the cards score rule
     */
    open fun updatePoints(points: Int) = points

    /**
     * Create a deep copy of a tile.
     *
     * @return The copied tile
     */
    open fun deepCopy() = Tile(
        connectors.toList(),
        connections.toList(),
        isEmpty,
        isEndTile
    )
}