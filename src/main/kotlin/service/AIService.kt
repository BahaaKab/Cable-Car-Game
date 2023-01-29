package service

import entity.*
import java.lang.IllegalStateException
import kotlin.math.abs

import kotlin.random.Random


data class WeightedPosition(val x: Int, val y: Int, var weight: Float = 0f) {
    val position = Pair(x, y)
}

data class PathSegment(val x: Int, val y: Int, val firstConnector: Int, val secondConnector: Int) {
    constructor(x: Int, y: Int, firstConnector: Int, tile: Tile) : this(
        x,
        y,
        firstConnector,
        if (tile.isEndTile) -1 else tile.connections[firstConnector]
    )

    val position = Pair(x, y)
    val connection = Pair(firstConnector, secondConnector)
}


class Path(var segments: MutableList<PathSegment>) {
    val size: Int
        get() = segments.size - (if (isClosed()) 2 else 1)

    init {
        require(segments.isNotEmpty() && segments.first().firstConnector == -1)
    }

    fun isClosed() = segments.last().secondConnector == -1

    fun isOpen() = !isClosed()

    fun copy() = Path(segments.map { it.copy() }.toMutableList())

    fun enhancedWith(tile: GameTile, x: Int, y: Int, board: Array<Array<Tile?>>): Path {
        val path = copy()
        var lastSegment = path.segments.last()
        val lastConnector = lastSegment.secondConnector

        val manhattanDist = abs(x - lastSegment.x) + abs(y - lastSegment.y)
        val isAdjacent = manhattanDist == 1
        val isConnectingToPath = (lastConnector in 0..1 && y == lastSegment.y - 1) ||
                (lastConnector in 2..3 && x == lastSegment.x + 1) ||
                (lastConnector in 4..5 && y == lastSegment.y + 1) ||
                (lastConnector in 6..7 && x == lastSegment.x - 1)

        // If the path is already closed or the tile is not adjacent to the last tile of the path, then the new path
        // can't be enhanced. Also if the tiles are adjacent but the path is not connecting, the path is not enhanced
        if (path.isClosed() || !isAdjacent || !isConnectingToPath) {
            return path
        }
        // Otherwise add the path segment of the tile to the path
        var nextSegment = PathSegment(x, y, tile.OUTER_TILE_CONNECTIONS[lastConnector], tile)
        path.segments.add(nextSegment)

        // Enhance the path with segments based on the current board state
        var currentTile: Tile?
        var nextX = x
        var nextY = y
        while (path.isOpen()) {
            lastSegment = path.segments.last()
            when (lastSegment.secondConnector) {
                0, 1 -> nextY--
                2, 3 -> nextX++
                4, 5 -> nextY++
                6, 7 -> nextX--
                else -> throw IllegalStateException()
            }
            currentTile = board[nextX][nextY]
            if (currentTile == null) {
                break
            }
            val nextFirstConnector = currentTile.OUTER_TILE_CONNECTIONS[lastSegment.secondConnector]
            val nextSecondConnector = if (currentTile.isEndTile) -1 else currentTile.connections[nextFirstConnector]
            nextSegment = PathSegment(nextX, nextY, nextFirstConnector, nextSecondConnector)
            path.segments.add(nextSegment)
        }
        return path
    }
}


/**
 *
 * Service layer class that provides the logic for artificial Intelligence and how it will behave based on difficulty.
 * @param [rootService] Connected root service
 */
class AIService(private val rootService: RootService) : AbstractRefreshingService() {
    var hardAIRules = listOf(::enhancesAIPaths, ::closesEnemyPath, ::closesOwnPath)

    /**
     * to get the best calculate for AI when set Difficulty to HARD
     *
     */
    fun makeAIMove() {
        when (rootService.cableCar.currentState.activePlayer.playerType) {
            PlayerType.AI_EASY -> easyTurn()
            PlayerType.AI_HARD -> hardTurn(hardAIRules)
            PlayerType.HUMAN -> return
        }
    }

    /**
     * checks board for random place to place a placeable tile
     *
     */
    private fun easyTurn(): Unit = with(rootService.playerActionService) {
        val validPositions = rootService.playerActionService.getValidPositions()

        // Does the randomized draw. For a turn and AI it is no difference when the card is drawn
        if (Random.nextBoolean()) {
            drawTile()
        }
        val tileToPlace = with(rootService.cableCar.currentState.activePlayer) {
            checkNotNull(currentTile ?: handTile)
        }

        if (rootService.cableCar.allowTileRotation) {
            for (i in 0..3) {
                tileToPlace.rotate(true)
                val placeableTiles = validPositions.minus(
                    getOnePointPositions(tileToPlace)
                )
                if (placeableTiles.isNotEmpty()) {
                    val position = placeableTiles.shuffled().first()
                    return placeTile(position.first, position.second)
                }
            }
            val position = validPositions.shuffled().first()
            return placeTile(position.first, position.second)
        } else {
            val placeableTiles = validPositions.minus(
                getOnePointPositions(tileToPlace)
            ).ifEmpty {
                validPositions
            }
            val position = placeableTiles.shuffled().first()
            return placeTile(position.first, position.second)
        }
    }


    /**
     * to find good tile for HARD AI that is allowed
     */
    private fun hardTurn(
        rules: List<(position: Pair<Int, Int>, tile: GameTile) -> Float>
    ): Unit = with(rootService) {
        val validPositions = playerActionService.getValidPositions()
        //
        val tileToPlace = with(cableCar.currentState.activePlayer) {
            checkNotNull(currentTile ?: handTile)
        }
        // If rotation is allowed, find the best position for the first rotation, that has placeable tiles.
        if (cableCar.allowTileRotation) {
            for (i in 0..3) {
                tileToPlace.rotate(true)
                val placeablePositions = validPositions - playerActionService.getOnePointPositions(tileToPlace)
                if (placeablePositions.isNotEmpty()) {
                    val (x, y) = getBestPosition(placeablePositions, tileToPlace, rules)
                    return playerActionService.placeTile(x, y)
                }
            }
            val (x, y) = getBestPosition(validPositions, tileToPlace, rules)
            return playerActionService.placeTile(x, y)
        }
        // If no rotation is allowed there will be always placeable positions. Find the best position of them
        val placeablePositions = (validPositions - playerActionService.getOnePointPositions(tileToPlace)).ifEmpty {
            validPositions
        }
        val (x, y) = getBestPosition(placeablePositions, tileToPlace, rules)
        rootService.playerActionService.placeTile(x, y)
    }


    /**
     * Get the best position from all placeable positions based on a set of rules. Positions with the highest weights will
     * be placed.
     *
     * @param placeablePositions All positions that are placeable as a Set of (x, y) Pairs
     * @param tile The GameTile that is selected to be placed
     * @param rules A rule has to calculate a weight for a single position and tile. The weight should be between
     * 0 and 1.
     *
     * @return The best position to place the current tile on. If no rules are specified, this will return a random
     * position.
     */
    private fun getBestPosition(
        placeablePositions: Set<Pair<Int, Int>>,
        tile: GameTile,
        rules: List<(position: Pair<Int, Int>, tile: GameTile) -> Float>
    ): Pair<Int, Int> {
        val weightedPositions = placeablePositions.map { (x, y) -> WeightedPosition(x, y) }
        // For each position calculate a weight based on a set of rules. The best position will be the one with the
        // highest weight.
        rules.forEach { rule ->
            weightedPositions.forEach { weightedPosition ->
                weightedPosition.weight += rule(weightedPosition.position, tile)
            }
            // println(weightedPositions)
        }
        // It might be that multiple positions have the same weight. In that case select one of them randomly
        val maxWeightPosition = weightedPositions.maxOf { it.weight }
        val bestPositions = weightedPositions.filter { weightedPosition ->
            weightedPosition.weight == maxWeightPosition
        }
        return bestPositions.shuffled().first().position
    }


    /**
     * =======================
     * ======== RULES ========
     * =======================
     */

    /**
     * How many paths of the AI will the position enhance?
     */
    private fun enhancesAIPaths(position: Pair<Int, Int>, tile: GameTile): Float {
        val (x, y) = position
        val totalPathLengthsAfterPlacement =
            rootService.cableCar.currentState.activePlayer.stationTiles.map { stationTile ->
                stationTile.getEnhancedPathWith(tile, x, y).size
            }.sumOf { it }
        val totalPathLengthsBeforePlacement =
            rootService.cableCar.currentState.activePlayer.stationTiles.map { stationTile ->
                stationTile.getPath().size
            }.sumOf { it }

        return (totalPathLengthsAfterPlacement - totalPathLengthsBeforePlacement).toFloat() /
                (totalPathLengthsBeforePlacement + 1)
    }


    private fun closesOwnPath(position: Pair<Int, Int>, tile: GameTile): Float {
        val powerStationBoost = if (position in listOf(Pair(4, 4), Pair(5, 4), Pair(4, 5), Pair(5, 5))) 2 else 1
        val threshold = 7
        val (x, y) = position
        return rootService.cableCar.currentState.activePlayer.stationTiles.map { stationTile ->
            val path = stationTile.getEnhancedPathWith(tile, x, y)
            if (path.segments.last().secondConnector != -1) {
                0
            } else {
                path.size * powerStationBoost  - threshold
            }
        }.sumOf { it } / threshold.toFloat()
    }


    private fun closesEnemyPath(position: Pair<Int, Int>, tile: GameTile): Float {
        val powerStationBoost = if (position in listOf(Pair(4, 4), Pair(5, 4), Pair(4, 5), Pair(5, 5))) 2 else 1
        val threshold = 4
        val (x, y) = position
        val enemyStations = rootService.cableCar.currentState.players.filter {
            it.name != rootService.cableCar.currentState.activePlayer.name
        }.flatMap { it.stationTiles }
        return enemyStations.map { stationTile ->
            val path = stationTile.getEnhancedPathWith(tile, x, y)
            // println(path)
            if (path.segments.last().secondConnector == -1) {
                threshold - path.size * powerStationBoost
            } else {
                0
            }
        }.sumOf { it } / threshold.toFloat()

    }

//    private fun closePathWithPowerStation(position: Pair<Int, Int>, tile: GameTile): Float {
//    }


    /**
     * ======================
     * ======================
     */

    private fun StationTile.getPath(): Path {
        val firstPosition = rootService.cableCarService.getPosition(this)
        val currentPosition = firstPosition.copyOf()
        var currentOuterConnector = startPosition
        val segments = path.map { tile ->
            when (currentOuterConnector) {
                0, 1 -> currentPosition[1]--
                2, 3 -> currentPosition[0]++
                4, 5 -> currentPosition[1]++
                6, 7 -> currentPosition[0]--
                else -> {}
            }
            val x = currentPosition[0]
            val y = currentPosition[1]
            val connectorA = OUTER_TILE_CONNECTIONS[currentOuterConnector]
            val connectorB = if (tile.isEndTile) {
                -1
            } else {
                tile.connections[connectorA]
            }
            val pathSegment = PathSegment(x, y, connectorA, connectorB)
            currentOuterConnector = connectorB
            pathSegment
        }.toMutableList()


        segments.add(0, PathSegment(firstPosition[0], firstPosition[1], -1, startPosition))
        return Path(segments)
    }

    private fun StationTile.getEnhancedPathWith(tile: GameTile, x: Int, y: Int) =
        getPath().enhancedWith(tile, x, y, rootService.cableCar.currentState.board)
}
