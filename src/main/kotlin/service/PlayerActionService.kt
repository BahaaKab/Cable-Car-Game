package service

import entity.*


/**
 * This class is used to manage actions of the [Player].
 */
class PlayerActionService(private val rootService: RootService) : AbstractRefreshingService() {

    /**
     * Undo the last game [State] and move on to the nextTurn.
     */
    fun undo() = with(rootService.cableCar) {
        if(gameMode == GameMode.NETWORK || history.undoStates.isEmpty()) { return }

        val players = currentState.players
        // Undo all player actions up to the current player's last action
        repeat(players.size){
            val undoState = history.undoStates.pop()
            history.redoStates.push(undoState)
        }
        currentState = history.undoStates.peek()

        onAllRefreshables { refreshAfterUndo() }

    }

    /**
     * Redo the last undone game [State] and move on to the nextTurn.
     */
    fun redo() = with(rootService.cableCar) {
        if(gameMode == GameMode.NETWORK || history.redoStates.isEmpty()) { return }

        // Redo all player actions up to the current player's previously done turn
        val players = currentState.players
        repeat(players.size) {
            val redoState = history.redoStates.pop()
            history.undoStates.push(redoState)
        }
        currentState = history.undoStates.peek()

        onAllRefreshables { refreshAfterRedo() }
    }

    /**
     * [Player] draws a [GameTile] in the case he wants an alternative [GameTile] or after he placed a [GameTile]
     * without having an alternative [GameTile]
     */
    fun drawTile() = with(rootService.cableCar.currentState) {
        if(drawPile.isEmpty()) return
        
        if (activePlayer.handTile == null) {
            activePlayer.handTile = drawPile.removeFirst()
        } else if (activePlayer.currentTile == null) {
            activePlayer.currentTile = drawPile.removeFirst()
        }
        onAllRefreshables { refreshAfterDrawTile() }
    }

    /**
     * [Player] places a [GameTile] on a given position. Either a [GameTile] on the Hand or an alternative [GameTile]
     * the [Player] has drawn.
     *
     * @param posX The x position
     * @param posY The y position
     *
     * @throws IllegalStateException If the player has no tile to place
     */
    fun placeTile(posX: Int, posY: Int) = with(rootService) {
        val player = cableCar.currentState.activePlayer
        val board = cableCar.currentState.board

        val tileToPlace = checkNotNull(player.currentTile ?: player.handTile)
        // If the position for the placement is either not valid or not legal while there are still legal placements
        // possible, return.
        // TODO: This needs some thoughts as a tile still needs to be adjacent to some other game or station tiles,
        //  even if no allowed position exists.
        val isValid = board[posX][posY] == null && isAdjacentToTiles(posX, posY)
        val isAllowed = !positionIsIllegal(posX, posY, tileToPlace) || onlyIllegalPositionsLeft(tileToPlace)
        if (!isValid || !isAllowed) {
            return
        }
        // Otherwise place the tile
        board[posX][posY] = tileToPlace
        // Refresh the GUI
        onAllRefreshables { refreshAfterPlaceTile(posX, posY) }
        // If the original hand tile was used, draw a new handTile, otherwise clear the currentTile
        if (player.currentTile == null) {
            player.handTile = null
            drawTile()
        } else { player.currentTile = null }
        // If this is a network game, create the turn message
        if (cableCar.gameMode == GameMode.NETWORK) {
            // TODO
        }
        // TODO: Shouldn't this move inside cableCarService.nextTurn()?
        cableCarService.updatePaths(posX,posY)
        cableCarService.calculatePoints()
        // Start the next turn
        cableCarService.nextTurn()
    }

    /**
     * checks if it's illegal to place [GameTile] on a special Position that's null.
     * In detail: It gets checked out if a closed path of length 1 gets constructed by placing the [GameTile]
     *
     * @param posX The x position
     * @param posY The y position
     * @param gameTile The tile to place
     *
     * @return Whether the position is illegal
     */
    fun positionIsIllegal(posX : Int, posY : Int, gameTile : GameTile) : Boolean {
        // Get all adjacent StationTiles
        val adjStationTiles = getAdjacentTiles(posX, posY).filterIsInstance<StationTile>()
        // A check for each adjacent [StationTile] if it forms a path of length 1
        for(stationTile in adjStationTiles){
            // A [StationTile] that can form a path of length 1 has to have an empty path at begin
            if(stationTile.path.isNotEmpty()){
                continue
            }
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
            val nextTile = rootService.cableCar.currentState.board[x][y]
            if(nextTile != null && nextTile.isEndTile){
                return true
            }
        }
        return false
    }

    /**
     * Determines if on every position the [GameTile] of the [Player] cannot be placed in a regular way. That means that
     * on every position in "the mid" (every position that isn't adjacent to a [StationTile]) is a [GameTile] and that
     * on the adjacent positions of [StationTile]s a [GameTile] can only be placed so that it constructs a closed path
     * of length 1.
     *
     * @param gameTile The game tile that the placement is checked against
     *
     * @return Whether all possible positions are illegal positions
     */
    fun onlyIllegalPositionsLeft(gameTile : GameTile) : Boolean = with(rootService.cableCar.currentState) {
         // First check if any Position in the mid is free. If a position p in the mid is free it always implies that
         // there has to be a legal position on the board even if the position p doesn't have any adjacent GameTile
        for(y in (2..7)){
            for(x in (2..7)){
                if(board[x][y] == null){
                    return false
                }
            }
        }
        // If every position in the mid isn't free try out each position that is adjacent to a StationTile
        for(y in (1..8)){
            if(board[1][y] == null && !positionIsIllegal(1,y,gameTile)){
                return false
            }
            if(board[8][y] == null && !positionIsIllegal(8,y,gameTile)){
                return false
            }
        }
        for(x in (1..8)){
            if(board[x][1] == null && !positionIsIllegal(x,1,gameTile)){
                return false
            }
            if(board[x][8] == null && !positionIsIllegal(x,8,gameTile)){
                return false
            }
        }
        return true
    }

    /**
     * Determines if a Tile can be placed at Position (posX,posY)
     *
     * @param posX The x position
     * @param posY The y position
     *
     * @return Whether at least one adjacent tile is a GameTile or a StationTile.
     */
    fun isAdjacentToTiles(posX: Int, posY : Int) =
        getAdjacentTiles(posX, posY).any { it is GameTile || it is StationTile }

    /**
     * Get the adjacent tiles to a given position.
     *
     * @param posX The x position
     * @param posY The y position
     *
     * @return The list of all adjacent tiles or null in case there is no adjacent Tile.
     */
    private fun getAdjacentTiles(posX: Int, posY: Int) : List<Tile?> = with(rootService.cableCar.currentState) {
        val adjLeft: Tile? = board[posX - 1][posY]
        val adjRight: Tile? = board[posX + 1][posY]
        val adjTop: Tile? = board[posX][posY - 1]
        val adjBot: Tile? = board[posX][posY + 1]
        return listOf(adjLeft, adjRight, adjTop, adjBot)
    }

    /**
     * Rotates the handTile of the current [Player] by 90° to the left-hand side.
     */
    fun rotateTileLeft() {
        rotateTile(clockwise = false)
        onAllRefreshables { refreshAfterRotateTileLeft() }
    }

    /**
     * Rotates the handTile of the current [Player] by 90° to the right-hand side.
     */
    fun rotateTileRight() {
        rotateTile(clockwise = true)
        onAllRefreshables { refreshAfterRotateTileRight() }
    }

    /**
     * Rotate a tile by 90°.
     *
     * @param clockwise Whether the direction of the rotation is clockwise or the other way around.
     *
     * @throws IllegalStateException If there is no tile to rotate.
     */
    private fun rotateTile(clockwise: Boolean = true) = with(rootService.cableCar.currentState.activePlayer) {
        // If the player has drawn a GameTile from the pile, it is set to the currentTile.
        // Otherwise he uses the handTile.
        val tileToRotate = currentTile ?: handTile
        // The shift index is used to rotate rotate the connections on a tile.
        with (checkNotNull(tileToRotate)) {
            val indexShift = if (clockwise) { 2 } else { connections.size - 2 }
            // First set the new values
            connections = connections.map { (it + indexShift) % connections.size }
            // Then set the values to the correct indices
            connections = List(connections.size) { connections[(it + indexShift+4) % connections.size] }
        }
    }


    /**
     * Changes the AISpeed in the [CableCar] class.
     */
    fun setAISpeed(speed: Int) {
        rootService.cableCar.AISpeed = speed
    }
}
