package service


import entity.*

@Suppress("UNUSED_PARAMETER","UNUSED","UndocumentedPublicFunction","UndocumentedPublicClass","EmptyFunctionBlock")
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
                val path : List<Tile> = stationTile.path!!
                // check if the path goes to another StationTile or to a PowerStationTile
                val pathIsClosed : Boolean = path[path.size-1].isEndTile
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
     * Method to calculate the next active [Player]
     */
    fun nextTurn() {
        val currentState : State = rootService.cableCar!!.currentState
        val playerList : List<Player> = currentState.players
        val currentActivePlayer : Player = currentState.activePlayer

        if(currentActivePlayer.equals(playerList.last())){
            currentState.activePlayer = playerList[0]
        }
        else{
            for(i in (0..playerList.size-1)){
                if(playerList[i].equals(currentActivePlayer)){
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
     * Method to calculate the winners of the game
     *
     * @return List of the winners
     */
    fun calculateWinners(): List<Player> {
        val currentState : State = rootService.cableCar!!.currentState
        val winnerList : MutableList<Player> = mutableListOf<Player>()
        val playerList : List<Player> = currentState.players
        playerList.sortedByDescending { it.score }
        val winnerScore : Int = playerList[0].score
        for(i in (0..playerList.size-1)){
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
     * @param [posX] position x on the grid
     * @param [posY] position y on the grid
     */
    fun updatePaths(tile: Tile?, posX: Int, posY: Int) {
        requireNotNull(tile)
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
                if(stationTiles.path!![stationTiles.path!!.size - 1] in adjacentTiles){
                    // To add is useless because in updatePath it gets added again
                    stationTiles.path!!.add(tile)
                    updatePath(stationTiles)
                }
            }
        }
    }

    /**
     * Private method which reconstructs the whole path for the player
     *
     * @param [StationTile] where the path begins
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
            if(connector == 0 || connector == 1){
                posY -= 1
            }
            if(connector == 2 || connector == 3){
                posX += 1
            }
            if(connector == 4 || connector == 5){
                posY += 1
            }
            if(connector == 6 || connector == 7){
                posX -= 1
            }
            val nextTile = currentState.board[posX][posY]
            if(nextTile == null){
                return
            }
            stationTile.path!!.add(nextTile)
            // Updating where the next starting connector is
            connector = when(connector){
                0 -> 5
                1 -> 4
                2 -> 7
                3 -> 6
                4 -> 1
                5 -> 0
                6 -> 3
                7 -> 2
                else -> -1 // doesn't happen anyway
            }
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
        var posX : Int = 0
        var posY : Int = 0

        for(y in (0..9)){
            for(x in (0..9)){
                if(gameBoard[x][y]!!.equals(stationTile)){
                    posX = x
                    posY = y
                }
            }
        }
        return arrayOf(posX,posY)
    }
}