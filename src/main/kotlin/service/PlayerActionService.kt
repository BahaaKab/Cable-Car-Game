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
        val undo : State = gameHistory.undoStates.pop()
        gameHistory.redoStates.add(undo)
        //Move on to the next turn
        rootService.cableCarService.nextTurn()
    }

    fun redo() {
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