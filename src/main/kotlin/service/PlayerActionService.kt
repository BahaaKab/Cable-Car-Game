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

    fun drawTile() {
    }

    fun placeTile(posX: Int, posY: Int) {
    }

    fun rotateTileLeft() {
    }

    fun rotateTileRight() {
    }

    fun setAISpeed(speed: Int) {
    }
}