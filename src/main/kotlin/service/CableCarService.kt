package service

import entity.*

class CableCarService(private val rootService: RootService) : AbstractRefreshingService() {

    /**
     * Method to calculate the points for each [Player] after a turn
     */
    fun calculatePoints() {
        val playerList : List<Player> = rootService.cableCar.currentState.players

        for(player in playerList){
            var score = 0
            // calculation for each path beginning from the StationTiles
            for(stationTile in player.stationTiles){
                val path : List<Tile> = stationTile.path
                // check if the path goes to another StationTile or to a PowerStationTile
                if(path.isEmpty()){
                    continue
                }
                val pathIsClosed : Boolean = path.last().isEndTile
                if(!pathIsClosed){
                    continue
                }
                var points = 0
                for(tile in path){
                    points = tile.updatePoints(points)
                }
                score += points
            }
            player.score = score
        }
    }

    /**
     * Update the current game state.
     */
    fun nextTurn() = with(rootService.cableCar) {
        // Check, if the game should end with the current game state
        if (isGameEnding()) {
            return endGame()
        }
        // Push the current game state to the history
        history.undoStates.push(currentState.deepCopy())

        // Set the next active player
        nextPlayer()

        onAllRefreshables { refreshAfterNextTurn() }

        // Decide what to do next based on the kind of player
        with(currentState.activePlayer) {
            if (isNetworkPlayer) {
                // TODO
                return
            }
            if(playerType == PlayerType.AI_EASY || playerType == PlayerType.AI_HARD) {
                rootService.aIService.doTurn()
                return
            }
        }

    }

    /**
     * Set the next active player for the upcoming turn.
     */
    private fun nextPlayer() : Unit = with(rootService.cableCar.currentState) {
        val indexOfActivePlayer = players.indexOf(activePlayer)
        activePlayer = players[(indexOfActivePlayer + 1) % players.size]
    }

    /**
     * Whether the game ends with the current [State]. This is the case, if there are no tiles to place anymore.
     *
     * @return Whether the game ends with the current [State]
     */
    private fun isGameEnding(): Boolean = rootService.cableCar.currentState.board.all { column ->
        column.filterNotNull().size == column.size
    }

    /**
     * Trigger the [view.EndScene]
     */
    private fun endGame() {
        onAllRefreshables { refreshAfterEndGame() }
    }

    /**
     * Calculates the winners of the current game
     *
     * @return List of the winners
     */
    fun calculateWinners(): List<Player> {
        val currentState : State = rootService.cableCar.currentState
        val winnerList : MutableList<Player> = mutableListOf()
        var playerList : List<Player> = currentState.players
        playerList = playerList.sortedByDescending { it.score }
        val winnerScore : Int = playerList[0].score
        for(i in playerList.indices){
            if(playerList[i].score == winnerScore){
                winnerList.add(playerList[i])
            }
        }
        return winnerList
    }

    /**
     * Method that selects each path that needs to get updated
     *
     * @param posX position x on the grid
     * @param posY position y on the grid
     */
    fun updatePaths(posX: Int, posY: Int) {
        val currentState : State = rootService.cableCar.currentState
        val playerList : List<Player> = rootService.cableCar.currentState.players
        val adjacentTiles : List<Tile?> = listOf(currentState.board[posX][posY-1],
                                                currentState.board[posX][posY+1],
                                                currentState.board[posX-1][posY],
                                                currentState.board[posX+1][posY])
        /**
         * For each [Player] and his [StationTile] is checked if the end of the
         * path ends in the adjacentTiles-List. In this case the path from the [StationTile]
         * has to be updated
         */
        for(player in playerList){
            for(stationTiles in player.stationTiles){
                if(stationTiles in adjacentTiles){
                    updatePath(stationTiles)
                }
                else if(stationTiles.path.isNotEmpty() && stationTiles.path.last() in adjacentTiles){
                    updatePath(stationTiles)
                }
            }
        }
    }

    /**
     * Private method which reconstructs the whole path for the player
     *
     * @param stationTile where the path begins
     */
    fun updatePath(stationTile: StationTile) {
        val currentState : State = rootService.cableCar.currentState
        stationTile.path = mutableListOf()
        val gridPosition : Array<Int> = getPosition(stationTile)
        var connector : Int = stationTile.startPosition
        var posX : Int = gridPosition[0]
        var posY : Int = gridPosition[1]

        while(true){
            // updating the positions of possible next [Tile]
            when(connector){
                TOP_LEFT, TOP_RIGHT -> posY -= 1
                RIGHT_TOP, RIGHT_BOT -> posX += 1
                BOT_RIGHT, BOT_LEFT -> posY += 1
                LEFT_TOP, LEFT_BOT -> posX -= 1
            }
            val nextTile = currentState.board[posX][posY]  ?: return
            stationTile.path.add(nextTile)
            // Updating where the next starting connector is
            if(nextTile.isEndTile){
                return
            }
            // the connector is firstly set on the beginning of the connection
            connector = nextTile.OUTER_TILE_CONNECTIONS[connector]
            // Updating the next connector which directs to another possible [Tile]
            connector = nextTile.connections[connector]
        }
    }

    /**
     * Private method, to get the position of the [StationTile] in the grid
     *
     * @param [stationTile]
     * @return Array of the position of the [StationTile]
     */
    fun getPosition(stationTile : StationTile) : Array<Int> {
        val gameBoard : Array<Array<Tile?>> = rootService.cableCar.currentState.board

        for(x in (1..8)){
            if(gameBoard[x][0] == stationTile){
                return arrayOf(x,0)
            }
            if(gameBoard[x][9] == stationTile){
                return arrayOf(x,9)
            }
        }
        for(y in (1..8)){
            if(gameBoard[0][y] == stationTile){
                return arrayOf(0,y)
            }
            if(gameBoard[9][y] == stationTile){
                return arrayOf(9,y)
            }
        }
        // This case will never happen because a searched [StationTile] will always be found
        return arrayOf()
    }
}