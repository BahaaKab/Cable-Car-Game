package service

import TurnMessage

/**
 *
 * Service layer class that provides the logic for artificial Intelligence and how it will behave based on difficulty.
 * @param [rootService] Connected root service
*/

@Suppress("UNUSED_PARAMETER","UNUSED","UndocumentedPublicFunction","UndocumentedPublicClass","EmptyFunctionBlock")

class AIService(private val rootService: RootService) : AbstractRefreshingService() {

//    val turn: TurnMessage? = null

    /**
     * to get the best calculate for AI when set Difficulty to HARD
     */
    fun getBestTurn(){
    }

    /**
     * to find random tile for EASY AI that is allowed
     */
    fun findRandomTile(){

    }

    /**
     * to find the score for each move while calculating
     */
    fun score(){

    }

    /**
     * to place the tile in hand or drawn tile for both difficulties
     */
    fun placeTile(){}

    /**
     * decide to either place the hand's tile or draw a tile for HARD AI
     */
    fun placeOrDraw(){

    }



}