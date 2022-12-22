package service


import entity.*

class CableCarService(private val rootService: RootService) : AbstractRefreshingService() {

    /**
     * Method to calculate the points for each [Player] after a turn
     */
    fun calculatePoints() {
        val playerList : List<Player> = rootService.cableCar!!.currentState.players

        for(player in playerList){
            var score : Int = 0
            // calculation for each path beginning from the StationTiles
            for(stationTile in player.stationTiles){
                val path : List<Tile> = stationTile.path
                // check if the path goes to another StationTile or to a PowerStationTile
                val pathIsClosed : Boolean = path.last().isEndTile
                if(!pathIsClosed){
                    continue
                }
                var points : Int = 0
                for(tile in path){
                    points = tile.updatePoints(points)
                }
                score += points
            }
            player.score = score
        }
    }

    /**
     * Calculates the next active [Player]
     */
    fun nextTurn() {
        val currentState : State = rootService.cableCar!!.currentState
        val playerList : List<Player> = currentState.players
        val currentActivePlayer : Player = currentState.activePlayer

        if(currentActivePlayer == playerList.last()){
            currentState.activePlayer = playerList[0]
        }
        else{
            for(i in playerList.indices){
                if(playerList[i] == currentActivePlayer){
                    currentState.activePlayer = playerList[i+1]
                }
            }
        }
        onAllRefreshables { refreshAfterNextTurn() }
    }

    fun endGame() {
        onAllRefreshables { refreshAfterEndGame() }
    }

    /**
     * Calculates the winners of the current game
     *
     * @return List of the winners
     */
    fun calculateWinners(): List<Player> {
        val currentState : State = rootService.cableCar!!.currentState
        val winnerList : MutableList<Player> = mutableListOf<Player>()
        val playerList : List<Player> = currentState.players
        playerList.sortedByDescending { it.score }
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
     * @param [Tile] (I think only posX and posY are needed)
     * @param posX position x on the grid
     * @param posY position y on the grid
     */
    fun updatePaths(posX: Int, posY: Int) {
        val currentState : State = rootService.cableCar!!.currentState
        val playerList : List<Player> = rootService.cableCar!!.currentState.players
        val adjacentTiles : List<Tile?> = listOf(currentState.board[posX][posY-1],
                                                currentState.board[posX][posY+1],
                                                currentState.board[posX-1][posY],
                                                currentState.board[posX+1][posY])
        /**
         * For each [Player] and his [StationTiles] is checked if the end of the
         * path ends in the adjacentTiles-List. In this case the path from the [StationTile]
         * has to be updated
         */
        for(player in playerList){
            for(stationTiles in player.stationTiles){
                if(stationTiles.path.last() in adjacentTiles){
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
    private fun updatePath(stationTile: StationTile) {
        val currentState : State = rootService.cableCar!!.currentState
        stationTile.path = mutableListOf()
        val gridPosition : Array<Int> = getPosition(stationTile)
        var connector : Int = stationTile.startPosition
        var posX : Int = gridPosition[0]
        var posY : Int = gridPosition[1]

        while(true){
            // updating the positions of possible next [Tile]
            when(connector){
                0,1 -> posY -= 1
                2,3 -> posX += 1
                4,5 -> posY += 1
                6,7 -> posX -= 1
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
     * @param [StationTile]
     * @return Array of the position of the [StationTile]
     */
    private fun getPosition(stationTile : StationTile) : Array<Int> {
        val gameBoard : Array<Array<Tile?>> = rootService.cableCar!!.currentState.board

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