package service
/*
 * A [TurnMessage] expects the following parameter on a [0..9][0..9] board:
 * @param posX: Int as x-coordinate
 * @param posY: Int as y-coordinate
 * @param fromSupply: Boolean if a card was drawn
 * @param rotation: Int in degree for 0°, 90°, 180° and 270° clockwise rotation
 */
import edu.udo.cs.sopra.ntf.GameStateVerificationInfo
import edu.udo.cs.sopra.ntf.TileInfo
import edu.udo.cs.sopra.ntf.TurnMessage
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
     * @return [TurnMessage] of selected AI
     */
    fun doTurn(): TurnMessage{
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
    private fun easyTurn(): TurnMessage = with(rootService.cableCar.currentState){
        var fromSupply: Boolean =  Random.nextBoolean()
        val legalPosArray = legalPositions()
        legalPosArray.shuffle()
        // Does the randomized draw. For a turn and AI it is no difference when the card is drawn
        fromSupply=draw(fromSupply)

        while (!legalPosArray.isEmpty()) {
            val (thisPosX, thisPosY) = legalPosArray.removeFirst()
            for (i in 1..4){
                if (placeablePosition(thisPosX,thisPosY)){
                    return placeCurrentTile(thisPosX,thisPosY,fromSupply)
                }
                if (rootService.cableCar.allowTileRotation){
                    break
                }
                rotateCurrentTileClockwise()
            }
        }
        throw IllegalStateException()
    }

    /** tries to draw a card and store in the according info. If the card is to play then it goes to currentCard
     *
     * @param [drawAndPlay] if Ai wants to draw a card to play it
     * @return if it was possible to draw a card
     */
    private fun draw(drawAndPlay: Boolean):Boolean = with(rootService.cableCar.currentState){
        if (drawPile.isEmpty()){
            activePlayer.currentTile = activePlayer.handTile
            activePlayer.handTile=null
            return false
        }
        if (drawAndPlay){
            activePlayer.currentTile = activePlayer.handTile
            activePlayer.handTile = drawPile.removeFirst()
            return true
        }
        activePlayer.currentTile = drawPile.removeFirst()
        return false

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
        return only1PointPositionsWithCurrentTile()
    }

    /**
     * checks if [only1PointPositionsWithCurrentTile] are available
     *
     * @return Boolean whether it is so
     */
    fun only1PointPositionsWithCurrentTile():Boolean = with(rootService.cableCar.currentState.activePlayer){
        val legalPos =  legalPositions()
        val tileToTest = currentTile!!.deepCopy()
        for (i in 1..4){
            legalPos.forEach {
                if(!isOnePointPosition(it.first,it.second)){
                    currentTile=tileToTest
                    return false
                }
            }
            if (!rootService.cableCar.allowTileRotation){
                break
            }
            rotateCurrentTileClockwise()
        }
        return true
    }



    /**
     * [isOnePointPosition] checks a given position if it contributes only 1 point.
     *
     * @param posX
     * @param posY
     * @return Boolean whether 1 point
     */
     fun isOnePointPosition(posX: Int, posY: Int):Boolean = with(rootService.cableCar.currentState.activePlayer.currentTile!!) {
        if (posX !in 1..8 || posY !in 1..8) {
            throw IllegalArgumentException()
        }
        //checks middle of board positions
        if ((posX in 2..7) && (posY in 2..7)) {
            return false
        }
        //checks if border of board position and connection would result in 1 Point
        when (posX) {
            1 -> {
                if (connections[6] == 7) {
                    return true
                }
            }

            8 -> {
                if (connections[2] == 3) {
                    return true
                }
            }
        }
        when (posY) {
            1 -> {
                if (connections[0] == 1) {
                    return true
                }
                if (connections[0] == 7) {
                    return true
                }
                if (connections[0] == 3) {
                    return true
                }
                if (connections[1] == 6) {
                    return true
                }
                if (connections[1] == 2) {
                    return true
                }

            }
            8 -> {
                if (connections[4] == 5) {
                    return true
                }
                if (connections[4] == 3) {
                    return true
                }
                if (connections[4] == 7) {
                    return true
                }
                if (connections[5] == 6) {
                    return true
                }
                if (connections[5] == 2) {
                    return true
                }
            }
        }
        return false
    }


    /**
     * [rotateCurrentTileClockwise] rotate a current Tile clockwise
     *
     */
    fun rotateCurrentTileClockwise() = with(rootService.cableCar.currentState.activePlayer.currentTile!!){
        //With the following formular we can rotate the tile by 90° to the right
        connections = connections.map { (it + 2) % connections.size }
        connections = List(connections.size) {index -> connections[(8+index-2)%8] }
        rotation = (rotation +90)%360
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
     * [actualGameStateVerificationInfo] constructs a GameStateVerificationInfo
     *
     * @return [GameStateVerificationInfo]
     */
    private fun actualGameStateVerificationInfo() = GameStateVerificationInfo(rootService.cableCar.currentState.playedTiles,
        List(rootService.cableCar.currentState.drawPile.size)
        {index -> rootService.cableCar.currentState.drawPile[index].id  },
        List(rootService.cableCar.currentState.players.size)
        {index -> rootService.cableCar.currentState.players[index].score })

    /**
     * to find good tile for HARD AI that is allowed
     */
    private fun hardTurn(): TurnMessage{
        return TurnMessage(0, 0, true, 0, GameStateVerificationInfo(rootService.cableCar.currentState.playedTiles,
            List(rootService.cableCar.currentState.drawPile.size){index -> rootService.cableCar.currentState.drawPile[index].id  },List(rootService.cableCar.currentState.players.size){index -> rootService.cableCar.currentState.players[index].score }))

    }


    /**
     * to find the score for each move while calculating
     */
    private fun calculateTileScore(posX: Int, posY: Int){

    }

    /**
     * to places the current Tile
     */
    private fun placeCurrentTile(posX: Int, posY: Int, fromSupply:Boolean) : TurnMessage = with(rootService.cableCar.currentState){
        val latestRotation=activePlayer.currentTile!!.rotation
        playedTiles.add(TileInfo(posX,posY,activePlayer.currentTile!!.id,activePlayer.currentTile!!.rotation))
        board[posX][posY]=activePlayer.currentTile
        activePlayer.currentTile=null
        return TurnMessage(posX, posY, fromSupply,latestRotation , actualGameStateVerificationInfo())
    }

    /**
     * decide to either place the hand's tile or draw a tile for HARD AI
     */
    private fun placeOrDraw(){


    }

}

