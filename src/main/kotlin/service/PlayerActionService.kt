package service

import entity.*

@Suppress("UNUSED_PARAMETER","UNUSED","UndocumentedPublicFunction","UndocumentedPublicClass","EmptyFunctionBlock")

class PlayerActionService(private val rootService: RootService) : AbstractRefreshingService() {

    /**
     * Undoes the last game [State] and moves on to the nextTurn.
     * **/
    fun undo() {
        //Create a local variable to refer to the History object
        val gameHistory = rootService.cableCar!!.history
        //get some undo-magic done
        if(gameHistory.undoStates.isNotEmpty()) {
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
        val currentPlayer : Player = rootService.cableCar!!.currentState.activePlayer
        val currentState : State = rootService.cableCar!!.currentState
        // In this case the Player gets a handTile after placing a Tile
        if(currentPlayer.handTile == null && currentState.drawPile.isNotEmpty()){
            currentPlayer.handTile = currentState.drawPile.first()
            currentState.drawPile.removeFirst()
            onAllRefreshables { refreshAfterDrawTile() }
        }
        else{
            // The player has got a handTile but wants to have an alternative currentTile
            if(currentPlayer.currentTile == null && currentState.drawPile.isNotEmpty()){
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
        val currentPlayer : Player = rootService.cableCar!!.currentState.activePlayer
        val currentState : State = rootService.cableCar!!.currentState
        // In this case the player has just one HandTile and hasn't drawn an alternative one
        if(currentPlayer.currentTile == null){
            currentPlayer.currentTile = currentPlayer.handTile!!.deepCopy()
            currentPlayer.handTile = null
            if(currentState.board[posX][posY] == null) {
                currentState.board[posX][posY] = currentPlayer.currentTile
            }
            drawTile()
            onAllRefreshables { refreshAfterPlaceTile() }
        }
        // In this case the player has one HandTile and has an alternative Tile as a currentTile
        else{
            if(currentState.board[posX][posX] == null){
                currentState.board[posX][posY] = currentPlayer.currentTile
            }
            currentPlayer.currentTile = null
            onAllRefreshables { refreshAfterPlaceTile() }
        }
    }

    fun rotateTileLeft() {
    }

    fun rotateTileRight() {
    }

    fun setAISpeed(speed: Int) {
    }
}