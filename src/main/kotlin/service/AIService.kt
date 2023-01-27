package service

import entity.GameTile
import entity.PlayerType
import kotlin.random.Random


class WeightedPosition(x: Int, y: Int, var weight: Float = 1f) {
    val position = Pair(x, y)
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
            PlayerType.AI_HARD -> hardTurn(::enhancesAIPaths, )
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
    ) : Unit = with(rootService)  {
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
     * 0 and 1. A value of null will mean, that the tile will be never placed, even if other rules might weight it
     * rather high.
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
                weightedPosition.weight *= rule(weightedPosition.position, tile)
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
        val maxEnhancement = 4f
        val (x, y) = position

        val adjacentTiles = listOf(
            rootService.cableCar.currentState.board[x][y - 1],
            rootService.cableCar.currentState.board[x][y + 1],
            rootService.cableCar.currentState.board[x - 1][y],
            rootService.cableCar.currentState.board[x + 1][y]
        )

        return with(rootService.cableCar.currentState.activePlayer) {
            stationTiles.filter { stationTile ->
                val connectedToStationTile = { stationTile.path.isEmpty() && stationTile in adjacentTiles }
                val connectedToStationTilePath = {
                    stationTile.path.isNotEmpty()
                            && !stationTile.path.last().isEndTile
                            && stationTile.path.last() in adjacentTiles
                }
                connectedToStationTile() || connectedToStationTilePath()
            }.size / maxEnhancement
        }


    }


    /**
     * decide to either place the hand's tile or draw a tile for HARD AI
     */
    private fun placeOrDraw() {
    }

}

