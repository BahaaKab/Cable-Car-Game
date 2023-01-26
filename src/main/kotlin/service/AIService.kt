package service

import entity.GameTile
import entity.PlayerType
import kotlin.random.Random


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
            PlayerType.AI_HARD -> hardTurn()
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
    private fun hardTurn() {
        if (rootService.cableCar.allowTileRotation) {
            easyTurn()
        }

        val tileToPlace = with(rootService.cableCar.currentState.activePlayer) {
            checkNotNull(currentTile ?: handTile)
        }
        val placeablePositions = rootService.playerActionService.getPlaceablePositions(tileToPlace, false)
        val weightedPositions = placeablePositions.zip(List(placeablePositions.size) { 1 }).map { (position, weight) ->
            val (x, y) = position
            var factor = 1
            if (enhancesAIPath(tileToPlace, x, y)) {
                factor += 1
            }


            Pair(position, weight * factor)
        }
        val bestPositions = weightedPositions.filter { weightedPosition ->
            weightedPosition.second == weightedPositions.maxOf { it.second }
        }
        // From the best positions select one randomly
        val (position, _) = bestPositions.shuffled().first()
        val (x, y) = position
        rootService.playerActionService.placeTile(x, y)
    }


    private fun enhancesAIPath(tile: GameTile, x: Int, y: Int): Boolean {
        val adjacentTiles = listOf(
            rootService.cableCar.currentState.board[x][y - 1],
            rootService.cableCar.currentState.board[x][y + 1],
            rootService.cableCar.currentState.board[x - 1][y],
            rootService.cableCar.currentState.board[x + 1][y]
        )

        return with(rootService.cableCar.currentState.activePlayer) {
            stationTiles.any { stationTile ->
                val connectedToStationTile = { stationTile.path.isEmpty() && stationTile in adjacentTiles }
                val connectedToStationTilePath = {
                    stationTile.path.isNotEmpty()
                            && !stationTile.path.last().isEndTile
                            && stationTile.path.last() in adjacentTiles
                }
                connectedToStationTile() || connectedToStationTilePath()
            }
        }
    }


    /**
     * decide to either place the hand's tile or draw a tile for HARD AI
     */
    private fun placeOrDraw() {
    }

}

