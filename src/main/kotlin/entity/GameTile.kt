package entity

import edu.udo.cs.sopra.ntf.Tile as NetworkTile
import edu.udo.cs.sopra.ntf.ConnectionInfo

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
class GameTile(val id: Int, connections: List<Int>) : Tile(
    List(8) { it }, connections, false, false
) {
    var rotation = 0

    init {
        require(connectors.size == 8)
        require(connections.size == 8)
    }

    /**
     * Increase the previously scored points by one.
     */
    override fun updatePoints(points: Int) = points + 1

    fun rotate(clockwise: Boolean) {
        val connectionsSize = connections.size
        rotation = (rotation + if (clockwise) { 90 } else { 270 }) % 360
        val indexShift = if (clockwise) { 2 } else { connectionsSize - 2 }
        // First set the new values
        connections = connections.map { (it + indexShift) % connectionsSize }
        // Then set the values to the correct indices
        connections = List(connectionsSize) { connections[(it + indexShift + 4) % connectionsSize] }
    }

    /**
     * TODO
     */
    override fun deepCopy(): GameTile {
        val copiedTile = GameTile(id, connections.toList())
        copiedTile.rotation = rotation
        return copiedTile
    }

    /**
     * TODO
     */
    fun toNetworkTile(): NetworkTile {
        // To make duplicate filtering easier, create the connection infos in a way, that the first value is always the
        // connector with the smaller value.
        val connectionInfos = connections.mapIndexed { index, value ->
            if (index <= value) {
                ConnectionInfo(index, value)
            } else {
                ConnectionInfo(value, index)
            }
        }
        // Sort the connection infos by the first connector
        connectionInfos.sortedBy { it.nodeOne }
        // At that point there should be eight connection infos, where always the connection info at position index / 2
        // is identical to the connection info at position index / 2 + 1. Therefore every connection at an even index
        // can be filtered out.
        connectionInfos.filterIndexed { index, _ -> index % 2 == 0 }
        return NetworkTile(id, connectionInfos)
    }


}