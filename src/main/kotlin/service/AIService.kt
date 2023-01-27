package service

import entity.GameTile
import entity.PlayerType
import entity.StationTile
import entity.Tile
import java.lang.IllegalStateException
import java.nio.file.Path
import kotlin.random.Random


class WeightedPosition(x: Int, y: Int, var weight: Float = 0f) {
    val position = Pair(x, y)
}

data class PathSegment(val x: Int, val y: Int, val firstConnector: Int, val secondConnector: Int) {
    val position = Pair(x, y)
    val connection = Pair(firstConnector, secondConnector)
}


/**
 *
 * Service layer class that provides the logic for artificial Intelligence and how it will behave based on difficulty.
 * @param [rootService] Connected root service
 */
class AIService(private val rootService: RootService) : AbstractRefreshingService() {
    /**
     * to get the best calculate for AI when set Difficulty to HARD
     *
     */
    fun makeAIMove() {
        when (rootService.cableCar.currentState.activePlayer.playerType) {
            PlayerType.AI_EASY -> easyTurn()
            PlayerType.AI_HARD -> hardTurn(::enhancesAIPaths)
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
        vararg rules: (position: Pair<Int, Int>, tile: GameTile) -> Float
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
                    val (x, y) = getBestPosition(placeablePositions, tileToPlace, *rules)
                    return playerActionService.placeTile(x, y)
                }
            }
            val (x, y) = getBestPosition(validPositions, tileToPlace, *rules)
            return playerActionService.placeTile(x, y)
        }
        // If no rotation is allowed there will be always placeable positions. Find the best position of them
        val placeablePositions = (validPositions - playerActionService.getOnePointPositions(tileToPlace)).ifEmpty {
            validPositions
        }
        val (x, y) = getBestPosition(placeablePositions, tileToPlace, *rules)
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
        vararg rules: (position: Pair<Int, Int>, tile: GameTile) -> Float
    ): Pair<Int, Int> {
        val weightedPositions = placeablePositions.map { (x, y) -> WeightedPosition(x, y) }
        // For each position calculate a weight based on a set of rules. The best position will be the one with the
        // highest weight.
        weightedPositions.forEach { weightedPosition ->
            rules.forEach { rule ->
                weightedPosition.weight += rule(weightedPosition.position, tile)
            }
        }
        // It might be that multiple positions have the same weight. In that case select one of them randomly
        val maxWeightPosition = weightedPositions.maxOf { it.weight }
        val bestPositions = weightedPositions.filter { weightedPosition ->
            weightedPosition.weight == maxWeightPosition
        }
        return bestPositions.shuffled().first().position
    }


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

//    private fun closesOwnPath(position: Pair<Int, Int>, tile: GameTile): Float {
//        rootService.cableCar.currentState.activePlayer.stationTiles.map { }
//    }

//    private fun closePathWithPowerStation(position: Pair<Int, Int>, tile: GameTile): Float {
//    }


    private fun StationTile.getPath(): List<PathSegment> {
        val currentPosition = rootService.cableCarService.getPosition(this)
        var currentOuterConnector = startPosition
        return path.map { tile ->
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
        }
    }

    private fun StationTile.pathAlreadyClosed() = path.isNotEmpty() && path.last().isEndTile

    private fun StationTile.getNeighbourPosition(): Pair<Int, Int> {
        val position = rootService.cableCarService.getPosition(this)
        when (startPosition) {
            0, 1 -> position[1]--
            2, 3 -> position[0]++
            4, 5 -> position[1]++
            6, 7 -> position[0]--
            else -> {}
        }
        return Pair(position[0], position[1])
    }

    private fun StationTile.getNeighboursOfLastPathElement(): Set<Pair<Int, Int>> {
        val path = getPath()
        return if (path.isEmpty()) {
            setOf(getNeighbourPosition())
        } else {
            setOf(
                Pair(path.last().x - 1, path.last().y),
                Pair(path.last().x + 1, path.last().y),
                Pair(path.last().x, path.last().y - 1),
                Pair(path.last().x, path.last().y + 1)
            )
        }
    }

    private fun StationTile.getEnhancedPathWith(
        tile: GameTile,
        x: Int,
        y: Int
    ): List<PathSegment> {
        val path = getPath().toMutableList()
        // If the path is already closed or if the placement position is not adjacent to the
        // last path element, the path will not be enhanced at all.
        if (pathAlreadyClosed() || Pair(x, y) !in getNeighboursOfLastPathElement()) {
            return path
        }
        var nextTile: Tile? = tile
        var lastPathSegment = if (path.isEmpty()) {
            val position = rootService.cableCarService.getPosition(this)
            PathSegment(position[0], position[1], -1, startPosition)
        } else {
            path.last()
        }
        do {
            val nextPosition = when (lastPathSegment.connection.second) {
                0, 1 -> Pair(lastPathSegment.x, lastPathSegment.y - 1)
                2, 3 -> Pair(lastPathSegment.x + 1, lastPathSegment.y)
                4, 5 -> Pair(lastPathSegment.x, lastPathSegment.y + 1)
                6, 7 -> Pair(lastPathSegment.x - 1, lastPathSegment.y)
                else -> throw IllegalStateException()
            }
            val nextConnectionA = OUTER_TILE_CONNECTIONS[lastPathSegment.connection.second]
            val nextConnectionB = nextTile!!.connections[nextConnectionA]
            lastPathSegment = PathSegment(nextPosition.first, nextPosition.second, nextConnectionA, nextConnectionB)
            path.add(lastPathSegment)
            nextTile = rootService.cableCar.currentState.board[nextPosition.first][nextPosition.second]


        } while (nextTile != null && !nextTile.isEndTile)
        return path
    }

    private fun StationTile.realDeepCopy(): StationTile {
        val copiedConnectors = connectors.map { it }
        val copiedPath = path.map { it.deepCopy() }.toMutableList()
        return StationTile(copiedConnectors).apply { path = copiedPath }
    }
}



