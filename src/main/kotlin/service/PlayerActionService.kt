package service

import entity.*

@Suppress("UNUSED_PARAMETER", "UNUSED", "UndocumentedPublicFunction", "UndocumentedPublicClass", "EmptyFunctionBlock")

class PlayerActionService(private val rootService: RootService) : AbstractRefreshingService() {

    /**
     * Undoes the last game [State] and moves on to the nextTurn.
     * **/
    fun undo() {
        //Create a local variable to refer to the History object
        val gameHistory = rootService.cableCar!!.history
        //get some undo-magic done
        if (gameHistory.undoStates.isNotEmpty()) {
            val undo: State = gameHistory.undoStates.pop()
            gameHistory.redoStates.push(undo)
            rootService.cableCar!!.currentState = undo
        }
        //Move on to the next turn
        rootService.cableCarService.nextTurn()
    }

    /**
     * Redos the last undone game [State] and moves on to the nextTurn.
     * **/
    fun redo() {
        //Create a local variable to refer to the History object
        val gameHistory = rootService.cableCar!!.history
        //get some redo-magic done
        if (gameHistory.redoStates.isNotEmpty()) {
            val redo: State = gameHistory.redoStates.pop()
            gameHistory.undoStates.push(redo)
            rootService.cableCar!!.currentState = redo
        }
        //Move on to the next turn
        rootService.cableCarService.nextTurn()
    }

    /**
     * [Player] draws a [GameTile] in the case he wants an alternative [GameTile] or after he placed a [GameTile]
     * without having an alternative [GameTile]
     */
    fun drawTile() {
        val currentPlayer: Player = rootService.cableCar!!.currentState.activePlayer
        val currentState: State = rootService.cableCar!!.currentState
        // In this case the Player gets a handTile after placing a Tile
        if (currentPlayer.handTile == null && currentState.drawPile.isNotEmpty()) {
            currentPlayer.handTile = currentState.drawPile.first()
            currentState.drawPile.removeFirst()
            onAllRefreshables { refreshAfterDrawTile() }
        } else {
            // The player has got a handTile but wants to have an alternative currentTile
            if (currentPlayer.currentTile == null && currentState.drawPile.isNotEmpty()) {
                currentPlayer.currentTile = currentState.drawPile.first()
                currentState.drawPile.removeFirst()
                onAllRefreshables { refreshAfterDrawTile() }
            }
        }
    }

    /**
     * [Player] places a [GameTile] on a given position. Either a [GameTile] on the Hand or an alternative [GameTile]
     * the [Player] has drawn
     *
     * @param posX
     * @param posY
     */
    fun placeTile(posX: Int, posY: Int) {
        val currentPlayer: Player = rootService.cableCar!!.currentState.activePlayer
        val currentState: State = rootService.cableCar!!.currentState
        // In this case the player has just one HandTile and hasn't drawn an alternative one
        if (currentPlayer.currentTile == null) {
            currentPlayer.currentTile = currentPlayer.handTile!!.deepCopy()
            currentPlayer.handTile = null
            if (currentState.board[posX][posY] == null && isAdjacentToTiles(posX, posY)) {
                currentState.board[posX][posY] = currentPlayer.currentTile
            }
            drawTile()
            onAllRefreshables { refreshAfterPlaceTile() }
        }
        // In this case the player has one HandTile and has an alternative Tile as a currentTile
        else {
            if (currentState.board[posX][posX] == null && isAdjacentToTiles(posX, posY)) {
                currentState.board[posX][posY] = currentPlayer.currentTile
            }
            currentPlayer.currentTile = null
            onAllRefreshables { refreshAfterPlaceTile() }
        }
    }
    
    /**
     * Determines if a Tile can be placed at Position (posX,posY)
     *
     * @param posX
     * @param posY
     *
     * @return Boolean
     */
    private fun isAdjacentToTiles(posX: Int, posY : Int) : Boolean{
        val currentState : State = rootService.cableCar!!.currentState
        val adj1 : Tile? = currentState.board[posX-1][posY]
        val adj2 : Tile? = currentState.board[posX+1][posY]
        val adj3 : Tile? = currentState.board[posX][posY-1]
        val adj4 : Tile? = currentState.board[posX][posY+1]
        val adjList : List<Tile?> = mutableListOf(adj1,adj2,adj3,adj4)
        var bool : Boolean = false
        for(i in adjList.indices){
            if(adjList[i] is GameTile || adjList[i] is StationTile){
                bool = true
            }
        }
        return bool
    }

    /**
     * Rotates the handtile of the current [Player] by 90° to the left handside.
     * */
    fun rotateTileLeft() {
        val activePlayer = rootService.cableCar!!.currentState.activePlayer
        //Safety mesure to ensure that the current actually has a handtile.
        val handTile = checkNotNull(activePlayer.handTile)
        //We need to edit content in the connections list...
        //Does this actually change something in on the entity layer?
        val connections = handTile.connections
        //With the following formular we can rotate the tile by 90° to the left
        handTile.connections = connections.map { (it + 2) % connections.size }
        onAllRefreshables { refreshAfterRotateTileLeft() }
    }

    /**
     * Rotates the handtile of the current [Player] by 90° to the right handside.
     * */
    fun rotateTileRight() {
        val activePlayer = rootService.cableCar!!.currentState.activePlayer
        //Safety mesure to ensure that the current actually has a handtile.
        val handTile = checkNotNull(activePlayer.handTile)
        //We need to edit content in the connections list...
        //Does this actually change something in on the entity layer?
        val connections = handTile.connections
        //With the following formular we can rotate the tile by 90° to the right
        handTile.connections = connections.map { (it - 2) % connections.size }
        onAllRefreshables { refreshAfterRotateTileRight() }
    }

    /**
     * Changes the AISpeed in the [CableCar] class.
     * **/
    fun setAISpeed(speed: Int) {
        val game = rootService.cableCar!!
        game.AISpeed = speed
    }
}