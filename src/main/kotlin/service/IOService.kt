package service

import entity.GameTile
import java.io.FileNotFoundException

/**
 * The absolute path pointing to the tiles.csv file in the resources directory.
 */
const val TILES_CSV_PATH : String = "/tiles.csv"

/**
 * A service class to handle IO actions.
 *
 * @property tilesCSVFile The tiles.csv file containing all information to generate the [GameTile]s.
 *
 * @constructor Creates an [IOService].
 */
class IOService {
    private val tilesCSVFile = IOService::class.java.getResource(TILES_CSV_PATH) ?: throw FileNotFoundException()

    /**
     * Get all [GameTile]s from the tiles.csv.
     *
     * @return The [GameTile]s.
     */
    fun getTilesFromCSV() : List<GameTile> {
        val lines = tilesCSVFile.readText().lines()
        return lines.mapIndexed { index, line ->
            GameTile(id = index, connections = getConnectionsFromCSVLine(line))
        }
    }

    /**
     * Parse a line of the given connections format specified by the tiles.csv file to a [GameTile.connections] list.
     *
     * As an example, a connection might be specified as "(0, 1), (2, 7), (3, 5), (4, 6);" and then would correspond
     * to a List [1, 0, 7, 5, 6, 3, 4, 2].
     *
     * @param line The line to parse.
     *
     * @return The connections as needed by a [GameTile].
     */
    private fun getConnectionsFromCSVLine(line: String): List<Int> {
        val connections = Array(8) { -1 }
        // Convert the String to a List of Integers, assuming that every connection point is defined through
        // a single digit.
        val parsedDigitsAsIntegers = line.filter { it.isDigit() }.map { it.digitToInt() }
        // Map the connections to the internal format.
        for (i in 0 until 8 step 2) {
            val nodeA = parsedDigitsAsIntegers[i]
            val nodeB = parsedDigitsAsIntegers[i + 1]
            connections[nodeA] = nodeB
            connections[nodeB] = nodeA
        }
        return connections.toList()
    }
}