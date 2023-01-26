package service

import entity.GameTile
import entity.PlayerType
import entity.StationTile
import kotlin.random.Random

/**
 *
 * Service layer class that provides the logic for artificial Intelligence and how it will behave based on difficulty.
 * @param [rootService] Connected root service
 */

@Suppress("UNUSED_PARAMETER", "UNUSED", "UndocumentedPublicFunction", "UndocumentedPublicClass", "EmptyFunctionBlock")

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
        if (Random.nextBoolean()) { drawTile() }
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
        easyTurn()
    }


    /**
     * to find the score for each move while calculating
     */
    private fun calculateTileScore(posX: Int, posY: Int) {

    }

    /**
     * decide to either place the hand's tile or draw a tile for HARD AI
     */
    private fun placeOrDraw() {
    }

}

