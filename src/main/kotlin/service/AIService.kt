package service

import entity.*
import kotlin.random.Random

/**
 *
 * Service layer class that provides the logic for artificial Intelligence and how it will behave based on difficulty.
 * @param [rootService] Connected root service
*/

@Suppress("UNUSED_PARAMETER","UNUSED","UndocumentedPublicFunction","UndocumentedPublicClass","EmptyFunctionBlock")

class AIService(private val rootService: RootService) : AbstractRefreshingService() {
    // surround codes every direction of a Tile
    private val surround = listOf(-1,-2,+2,+1)



    /**
     * to get the best calculate for AI when set Difficulty to HARD
     *
     */
    fun makeAIMove(){
         when (rootService.cableCar.currentState.activePlayer.playerType){
            PlayerType.AI_EASY -> easyTurn()
            PlayerType.AI_HARD -> hardTurn()
            PlayerType.HUMAN -> return
        }
    }

    /**
     * checks board for random place to place a placeable tile
     *
     */
     private fun easyTurn() = with(rootService.cableCar.currentState) {
        val legalPosArray = legalPositions()
        legalPosArray.shuffle()
        // Does the randomized draw. For a turn and AI it is no difference when the card is drawn
        if (Random.nextBoolean()) {
            Thread.sleep(500)
            rootService.playerActionService.drawTile()
        }

        while (!legalPosArray.isEmpty()) {
            val (thisPosX, thisPosY) = legalPosArray.removeFirst()
            for (i in 1..4) {
                if (placeablePosition(thisPosX, thisPosY)) {
                    Thread.sleep(500)
                    rootService.playerActionService.placeTile(thisPosX, thisPosY)
                    return
                }
                if (!rootService.cableCar.allowTileRotation) {
                    break
                }
                rootService.playerActionService.rotateTileRight()
            }
        }
    }

    /**
     * give all [legalPositions] where you can place a tile
     *
     * @return [ArrayDeque<Pair<Int,Int>>] with board position of places adjacent to game/stationTiles
     */
    private fun legalPositions():ArrayDeque<Pair<Int,Int>> = with(rootService.cableCar.currentState) {
        val legalPos: ArrayDeque<Pair<Int,Int>> = ArrayDeque()
        for(x in (1..8  )){
            for (y in 1..8)
            if(board[x][y] == null && isAdjacent(x,y)){
                legalPos.add(Pair(x,y))
            }
        }
        return legalPos
    }

    /**
     * [placeablePosition] checks a given position if it's legal to place a Tile there.
     *
     * @param posX
     * @param posY
     * @return Boolean whether placeable
     */
    fun placeablePosition(posX: Int, posY: Int) : Boolean = with(rootService.cableCar.currentState.activePlayer){
        if (rootService.cableCar.currentState.board[posX][posX] != null){
            return false
        }

        if(!isAdjacent(posX,posY)) {
            return false
        }

        if (!isOnePointPosition(posX,posY)){
            return true
        }
        return only1PointPositions()
    }

    /**
     * checks if [only1PointPositions] are available
     *
     * @return Boolean whether it is so
     */
    fun only1PointPositions():Boolean = with(rootService.cableCar.currentState.activePlayer){
        val legalPos =  legalPositions()
        var allIllegal = true
        for (i in 1..4){
            run legalfound@ {legalPos.forEach {
                if(!isOnePointPosition(it.first,it.second)){
                    allIllegal=false
                    return@legalfound
                } }
            }
            if (!rootService.cableCar.allowTileRotation){
                break
            }
            rootService.playerActionService.rotateTileRight()
        }
        return allIllegal
    }



    /**
     * [isOnePointPosition] checks a given position if it contributes only 1 point.
     *
     * @param posX
     * @param posY
     * @return Boolean whether 1 point
     */
     fun isOnePointPosition(posX: Int, posY: Int):Boolean = with(rootService.cableCar.currentState.activePlayer) {
        if (posX !in 1..8 || posY !in 1..8) {
            throw IllegalArgumentException()
        }
        //checks middle of board positions
        if ((posX in 2..7) && (posY in 2..7)) {
            return false
        }
        val tileToTest = checkNotNull(currentTile ?: handTile)
        //checks if border of board position and connection would result in 1 Point
        when (posX) {
            1 -> {
                if (tileToTest.connections[6] == 7) {
                    return true
                }
            }

            8 -> {
                if (tileToTest.connections[2] == 3) {
                    return true
                }
            }
        }
        when (posY) {
            1 -> {
                if (tileToTest.connections[0] == 1) {
                    return true
                }
                if (tileToTest.connections[0] == 7) {
                    return true
                }
                if (tileToTest.connections[0] == 3) {
                    return true
                }
                if (tileToTest.connections[1] == 6) {
                    return true
                }
                if (tileToTest.connections[1] == 2) {
                    return true
                }

            }
            8 -> {
                if (tileToTest.connections[4] == 5) {
                    return true
                }
                if (tileToTest.connections[4] == 3) {
                    return true
                }
                if (tileToTest.connections[4] == 7) {
                    return true
                }
                if (tileToTest.connections[5] == 6) {
                    return true
                }
                if (tileToTest.connections[5] == 2) {
                    return true
                }
            }
        }
        return false
    }


    /**
     * [isAdjacent] checks a given if adjacent to gameTile or stationTile
     *
     * @param posX
     * @param posY
     * @return Boolean whether is adjacent
     */
    private fun isAdjacent(posX: Int, posY: Int):Boolean = with(rootService.cableCar.currentState){
        return surround.any { board[posX+it%2][posY-it/2] is GameTile || board[posX+it%2][posY-it/2] is StationTile }
    }

    /**
     * to find good tile for HARD AI that is allowed
     */
    private fun hardTurn(){
        easyTurn()
    }


    /**
     * to find the score for each move while calculating
     */
    private fun calculateTileScore(posX: Int, posY: Int){

    }

    /**
     * decide to either place the hand's tile or draw a tile for HARD AI
     */
    private fun placeOrDraw(){


    }

}

