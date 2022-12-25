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
            // Checks if the position is legal
            if (currentState.board[posX][posY] == null && isAdjacentToTiles(posX, posY)
                && !positionIsIllegal(posX,posY,currentPlayer.currentTile!!)) {

                currentState.board[posX][posY] = currentPlayer.currentTile
                drawTile()
                rootService.cableCarService.updatePaths(posX,posY)
                rootService.cableCarService.calculatePoints()
                onAllRefreshables { refreshAfterPlaceTile() }
            }
            /*
             * Checks if the position is illegal. In that case a GameTile couldn't be placed. But it's also checked that
             * each position on the board is illegal. In that case the GameTile can be placed
             */
            else if(currentState.board[posX][posY] == null && isAdjacentToTiles(posX,posY)
                    && positionIsIllegal(posX,posY,currentPlayer.currentTile!!)
                    && onlyIllegalPositionsLeft(currentPlayer.currentTile!!)){

                currentState.board[posX][posY] = currentPlayer.currentTile
                drawTile()
                rootService.cableCarService.updatePaths(posX,posY)
                rootService.cableCarService.calculatePoints()
                onAllRefreshables { refreshAfterPlaceTile() }
            }
        }
        // In this case the player has one HandTile and has an alternative Tile as a currentTile
        else {
            // same cases as above
            if (currentState.board[posX][posX] == null && isAdjacentToTiles(posX, posY)
                && !positionIsIllegal(posX,posY,currentPlayer.currentTile!!)) {

                currentState.board[posX][posY] = currentPlayer.currentTile
                currentPlayer.currentTile = null
                rootService.cableCarService.updatePaths(posX,posY)
                rootService.cableCarService.calculatePoints()
                onAllRefreshables { refreshAfterPlaceTile() }
            }
            else if(currentState.board[posX][posY] == null && isAdjacentToTiles(posX,posY)
                && positionIsIllegal(posX,posY,currentPlayer.currentTile!!)
                && onlyIllegalPositionsLeft(currentPlayer.currentTile!!)){

                currentState.board[posX][posY] = currentPlayer.currentTile
                currentPlayer.currentTile = null
                rootService.cableCarService.updatePaths(posX,posY)
                rootService.cableCarService.calculatePoints()
                onAllRefreshables { refreshAfterPlaceTile() }
            }
        }
    }

    /**
     * checks if it's illegal to place [GameTile] on a special Position that's null.
     * In detail: It gets checked out if a closed path of length 1 gets constructed by placing the [GameTile]
     *
     * @param posX
     * @param posY
     * @param gameTile
     *
     * @return if it's a legal position or not
     */
    private fun positionIsIllegal(posX : Int, posY : Int, gameTile : GameTile) : Boolean{
        val currentState : State = rootService.cableCar!!.currentState
        val adj1 : Tile? = currentState.board[posX-1][posY]
        val adj2 : Tile? = currentState.board[posX+1][posY]
        val adj3 : Tile? = currentState.board[posX][posY-1]
        val adj4 : Tile? = currentState.board[posX][posY+1]
        val adjList : List<Tile?> = mutableListOf(adj1,adj2,adj3,adj4)
        // A list for all adjacent [StationTiles]
        val adjStationTiles : MutableList<StationTile> = mutableListOf()
        for(i in adjList.indices){
            if(adjList[i] is StationTile){
                adjStationTiles.add(adjList[i] as StationTile)
            }
        }
        var pathLengthOne : Boolean = false
        // A check for each adjacent [StationTile] if it forms a path of length 1
        for(stationTile in adjStationTiles){
            // A [StationTile] that can form a path of length 1 has to have an empty path at begin
            if(stationTile.path.size == 0){
                val startConnectorGameTile : Int = stationTile.OUTER_TILE_CONNECTIONS[stationTile.startPosition]
                val endConnectorGameTile : Int = gameTile.OUTER_TILE_CONNECTIONS[startConnectorGameTile]
                var x : Int = posX
                var y : Int = posY
                when(endConnectorGameTile){
                    TOP_LEFT, TOP_RIGHT -> y -= 1
                    RIGHT_TOP, RIGHT_BOT -> x += 1
                    BOT_RIGHT, BOT_LEFT -> y += 1
                    LEFT_TOP, LEFT_BOT -> x -= 1
                }
                val nextTile = currentState.board[x][y]
                if(nextTile!!.isEndTile){
                    pathLengthOne = true
                }
            }
        }
        return pathLengthOne
    }

    /**
     * Determines if on every position the [GameTile] of the [Player] cannot be placed in a regular way. That means that
     * on every position in "the mid" (every position that isn't adjacent to a [StationTile]) is a [GameTile] and that
     * on the adjacent positions of [StationTile]s a [GameTile] can only be placed so that it constructs a closed path
     * of length 1.
     *
     * @param gameTile
     *
     * @return boolean value if there exist only illegal positions
     */
    private fun onlyIllegalPositionsLeft(gameTile : GameTile) : Boolean{
        val currentState : State = rootService.cableCar!!.currentState
        var bool : Boolean = true
        /*
         * First check if any Position in the mid is free. If a position p in the mid is free it always implies that
         * there has to be a legal position on the board even if the position p doesn't have any adjacent GameTile
         */
        for(y in (2..7)){
            for(x in (2..7)){
                if(currentState.board[x][y] == null){
                    bool = false
                    break
                }
            }
            if(!bool){
                break
            }
        }
        if(!bool){
            return false
        }
        /*
         * If every position in the mid isn't free try out each position that is adjacent to a StationTile
         */
        for(y in (1..8)){
            if(currentState.board[1][y] == null && !positionIsIllegal(1,y,gameTile)){
                bool = false
                break
            }
            if(currentState.board[8][y] == null && !positionIsIllegal(8,y,gameTile)){
                bool = false
                break
            }
        }
        if(!bool){
            return false
        }
        for(x in (1..9)){
            if(currentState.board[x][1] == null && !positionIsIllegal(x,1,gameTile)){
                bool = false
                break
            }
            if(currentState.board[x][8] == null && !positionIsIllegal(x,8,gameTile)){
                bool = false
                break
            }
        }
        if(!bool){
            return false
        }
        return true
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
