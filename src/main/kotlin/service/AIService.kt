package service

/*
 * A [TurnMessage] expects the following parameter on a [0..9][0..9] board:
 * @param posX: Int as x-coordinate
 * @param posY: Int as y-coordinate
 * @param fromSupply: Boolean if a card was drawn
 * @param rotation: Int in degree for 0°, 90°, 180° and 270° clockwise rotation
 */
import TurnMessage
import entity.GameTile
import entity.Player
import entity.PlayerType
import entity.State
import kotlin.random.Random

/**
 *
 * Service layer class that provides the logic for artificial Intelligence and how it will behave based on difficulty.
 * @param [rootService] Connected root service
*/

@Suppress("UNUSED_PARAMETER","UNUSED","UndocumentedPublicFunction","UndocumentedPublicClass","EmptyFunctionBlock")

class AIService(private val rootService: RootService) : AbstractRefreshingService() {



    /**
     * to get the best calculate for AI when set Difficulty to HARD
     *
     * @return [TurnMessage] of selected AI
     */
    fun getTurn(): TurnMessage{
        return when (rootService.cableCar.currentState.activePlayer.playerType){
            PlayerType.AI_EASY -> easyTurn()
            PlayerType.AI_HARD -> hardTurn()
            PlayerType.HUMAN -> throw IllegalAccessError()
        }
    }

    /**
     * checks board for random place to place a placeable tile
     *
     * @return [TurnMessage] with random placement
     */
    private fun easyTurn(): TurnMessage{
        val currentState : State = rootService.cableCar.currentState
        val fromSupply =  Random.nextBoolean()
        val legalPos: ArrayDeque<Int> = ArrayDeque()
        val legalPosAfterDraw: ArrayDeque<Int> = ArrayDeque()

        // board coded as 1-dim integer
        for(z in (0..63)){
                if(currentState.board[1+ (z/8)][1+ (z%8)] == null){
                    legalPos.add(z)
                    legalPosAfterDraw.add(z)
                }
        }
        legalPos.shuffle()

        if (fromSupply){
            rootService.playerActionService.drawTile()
        }


        while (!legalPos.isEmpty()) {
            val actualPos= legalPos.removeFirst()
            val thisPosX: Int = 1+ (actualPos/8)
            val thisPosY: Int = 1+ (actualPos%8)

            if (placeablePosition(thisPosX,thisPosY)){
                return TurnMessage(thisPosX, thisPosY, fromSupply, 0)

            }
            if (rootService.cableCar.allowTileRotation){
                for(i in (1..3)){
                    rootService.playerActionService.rotateTileRight()
                    if (placeablePosition(thisPosX,thisPosY)){
                        return TurnMessage(thisPosX, thisPosY, fromSupply, i*90)
                    }

                }
            }
        }

        throw IllegalStateException()
    }


    /**
     * [AI] checks a given position if it's legal to place a Tile there.
     *
     * @param posX
     * @param posY
     * @return Boolean wheather placeable
     */
    fun placeablePosition(posX: Int, posY: Int) : Boolean {
        val currentPlayer: Player = rootService.cableCar.currentState.activePlayer
        val currentState: State = rootService.cableCar.currentState
        // In this case the player has just one HandTile and hasn't drawn an alternative one
        if (currentPlayer.currentTile == null) {
            currentPlayer.currentTile = currentPlayer.handTile!!.deepCopy()
            currentPlayer.handTile = null
            // Checks if the position is legal
            if (currentState.board[posX][posY] == null && rootService.playerActionService.isAdjacentToTiles(posX, posY)
                && !rootService.playerActionService.positionIsIllegal(posX,posY,currentPlayer.currentTile!!)) {

                return true
            }
            /*
             * Checks if the position is illegal. In that case a GameTile couldn't be placed. But it's also checked that
             * each position on the board is illegal. In that case the GameTile can be placed
             */
            else if(currentState.board[posX][posY] == null && rootService.playerActionService.isAdjacentToTiles(posX,posY)
                && rootService.playerActionService.positionIsIllegal(posX,posY,currentPlayer.currentTile!!)
                && rootService.playerActionService.onlyIllegalPositionsLeft(currentPlayer.currentTile!!)){

                return true
            }
            /*
             * Otherwise the player can't place the tile at the chosen position. In this case the currentTile and
             * the handTile get swapped. For example that the player can draw an alternative gameTile
             */
            else{
                currentPlayer.handTile = currentPlayer.currentTile
                currentPlayer.currentTile = null
                return false
            }
        }
        // In this case the player has one HandTile and has an alternative Tile as a currentTile
        else {
            // same cases as above
            if (currentState.board[posX][posX] == null && rootService.playerActionService.isAdjacentToTiles(posX, posY)
                && !rootService.playerActionService.positionIsIllegal(posX,posY,currentPlayer.currentTile!!)) {

                return true
            }
            else if(currentState.board[posX][posY] == null && rootService.playerActionService.isAdjacentToTiles(posX,posY)
                && rootService.playerActionService.positionIsIllegal(posX,posY,currentPlayer.currentTile!!)
                && rootService.playerActionService.onlyIllegalPositionsLeft(currentPlayer.currentTile!!)){

                return true
            }
        }
        return false
    }

    /**
     * to find good tile for HARD AI that is allowed
     */
    private fun hardTurn(): TurnMessage{
        return TurnMessage(0,0,true,0 )
    }

    /**
     * to find the score for each move while calculating
     */
    private fun calculateTileScore(posX: Int, posY: Int){

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