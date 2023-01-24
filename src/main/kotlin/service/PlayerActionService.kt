package service

import edu.udo.cs.sopra.ntf.TileInfo
import entity.*
import tools.aqua.bgw.core.BoardGameApplication


/**
 * This class is used to manage actions of the [Player].
 */
class PlayerActionService(private val rootService: RootService) : AbstractRefreshingService() {

    /**
     * Undo the last game [State] and move on to the nextTurn.
     */
    fun undo() = with(rootService.cableCar) {
        if (gameMode == GameMode.NETWORK || history.undoStates.isEmpty() ||
            history.undoStates.size <= currentState.players.size
        ) {
            return
        }

        val oldState = currentState.deepCopy()

        // Undo all player actions up to the current player's last action
        repeat(currentState.players.size) {
            val undoState = history.undoStates.pop()
            history.redoStates.push(undoState)
        }
        currentState = history.undoStates.peek().deepCopy()

        for (player in currentState.players) {
            for (stationTiles in player.stationTiles) {
                rootService.cableCarService.updatePath(stationTiles)
            }
        }

        onAllRefreshables { refreshAfterUndo(oldState) }
    }

    /**
     * Redo the last undone game [State] and move on to the nextTurn.
     */
    fun redo() = with(rootService.cableCar) {
        if (gameMode == GameMode.NETWORK || history.redoStates.isEmpty()) {
            return
        }


        val oldState = currentState.deepCopy()

        // Redo all player actions up to the current player's previously done turn
        repeat(currentState.players.size) {
            val redoState = history.redoStates.pop()
            history.undoStates.push(redoState)
        }
        currentState = history.undoStates.peek().deepCopy()

        for (player in currentState.players) {
            for (stationTiles in player.stationTiles) {
                rootService.cableCarService.updatePath(stationTiles)
            }
        }

        onAllRefreshables { refreshAfterRedo(oldState) }
    }

    /**
     * [Player] draws a [GameTile] in the case he wants an alternative [GameTile] or after he placed a [GameTile]
     * without having an alternative [GameTile]
     */
    fun drawTile() = with(rootService.cableCar.currentState) {
        if (drawPile.isEmpty()) return

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

        val fromSupply = player.currentTile != null
        val tileToPlace = checkNotNull(player.currentTile ?: player.handTile)

        // It is never ever allowed to place a tile somewhere without any adjacent Game or Station Tiles
        val isValid = board[posX][posY] == null && isAdjacentToTiles(posX, posY)
        if (!isValid) {
            return
        }

        val isAllowed: Boolean
        if (!rootService.cableCar.allowTileRotation) {
            isAllowed = !positionIsIllegal(posX, posY, tileToPlace) || onlyIllegalPositionsLeft(tileToPlace)
        } else {
            // check if all positions are illegal even with all rotation-forms
            val b1 = onlyIllegalPositionsLeft(tileToPlace)
            rotateTile(true)
            val b2 = onlyIllegalPositionsLeft(tileToPlace)
            rotateTile(true)
            val b3 = onlyIllegalPositionsLeft(tileToPlace)
            rotateTile(true)
            val b4 = onlyIllegalPositionsLeft(tileToPlace)
            // The Tile is now as it was before
            rotateTile(true)

            // placing the tile is only possible if the position is legal or if for all tile rotations every grid
            // position is still illegal
            isAllowed = !positionIsIllegal(posX, posY, tileToPlace) || (b1 && b2 && b3 && b4)
        }

        if (!isAllowed) {
            return
        }

        // Otherwise place the tile
        board[posX][posY] = tileToPlace
        cableCar.currentState.placedTiles.add(
            TileInfo(posX, posY, tileToPlace.id, tileToPlace.rotation)
        )
        // Refresh the GUI
        onAllRefreshables { refreshAfterPlaceTile(posX, posY) }
        // If the original hand tile was used, draw a new handTile, otherwise clear the currentTile
        if (player.currentTile == null) {
            player.handTile = null
            drawTile()
        } else {
            player.currentTile = null
        }
        // If this is a network game, create the turn message
        if (cableCar.gameMode == GameMode.NETWORK && rootService.networkService.networkClient.playerName == cableCar.currentState.activePlayer.name) {
            networkService.sendTurnMessage(posX, posY, fromSupply, tileToPlace.rotation)
        }
        // TODO: Shouldn't this move inside cableCarService.nextTurn()?
        cableCarService.updatePaths(posX, posY)
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
    fun positionIsIllegal(posX: Int, posY: Int, gameTile: GameTile): Boolean {
        // Get all adjacent StationTiles
        val adjStationTiles = getAdjacentTiles(posX, posY).filterIsInstance<StationTile>()
        // A check for each adjacent [StationTile] if it forms a path of length 1
        for (stationTile in adjStationTiles) {
            // A [StationTile] that can form a path of length 1 has to have an empty path at begin
            if (stationTile.path.isNotEmpty()) {
                continue
            }
            val startConnectorGameTile: Int = stationTile.OUTER_TILE_CONNECTIONS[stationTile.startPosition]
            val endConnectorGameTile: Int = gameTile.connections[startConnectorGameTile]
            var x: Int = posX
            var y: Int = posY
            when (endConnectorGameTile) {
                TOP_LEFT, TOP_RIGHT -> y -= 1
                RIGHT_TOP, RIGHT_BOT -> x += 1
                BOT_RIGHT, BOT_LEFT -> y += 1
                LEFT_TOP, LEFT_BOT -> x -= 1
            }
            val nextTile = rootService.cableCar.currentState.board[x][y]
            if (nextTile != null && nextTile.isEndTile) {
                return true
            }
        }
        return false
    }

    /**
     * Whether there are no options to place a tile other than closing a path of length 1.
     *
     * @param gameTile The game tile that the placement is checked against
     *
     * @return Whether all possible positions are illegal positions, in the sense that they would close a path of
     * length 1.
     */
    fun onlyIllegalPositionsLeft(gameTile: GameTile): Boolean = with(rootService.cableCar.currentState) {
        for (y in (1..8)) {
            if (board[1][y] == null && !positionIsIllegal(1, y, gameTile)) {
                return false
            }
            if (board[8][y] == null && !positionIsIllegal(8, y, gameTile)) {
                return false
            }
        }
        for (x in (1..8)) {
            if (board[x][1] == null && !positionIsIllegal(x, 1, gameTile)) {
                return false
            }
            if (board[x][8] == null && !positionIsIllegal(x, 8, gameTile)) {
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
    fun isAdjacentToTiles(posX: Int, posY: Int) =
        getAdjacentTiles(posX, posY).any { it is GameTile || it is StationTile }

    /**
     * Get the adjacent tiles to a given position.
     *
     * @param posX The x position
     * @param posY The y position
     *
     * @return The list of all adjacent tiles or null in case there is no adjacent Tile.
     */
    private fun getAdjacentTiles(posX: Int, posY: Int): List<Tile?> = with(rootService.cableCar.currentState) {
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
        if (!rootService.cableCar.allowTileRotation) return
        rotateTile(clockwise = false)
        onAllRefreshables { refreshAfterRotateTileLeft() }
    }

    /**
     * Rotates the handTile of the current [Player] by 90° to the right-hand side.
     */
    fun rotateTileRight() {
        if (!rootService.cableCar.allowTileRotation) return
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
        with(checkNotNull(tileToRotate)) {
            rotation = (rotation + if (clockwise) {
                90
            } else {
                270
            }) % 360
            val indexShift = if (clockwise) {
                2
            } else {
                connections.size - 2
            }
            // First set the new values
            connections = connections.map { (it + indexShift) % connections.size }
            // Then set the values to the correct indices
            connections = List(connections.size) { connections[(it + indexShift + 4) % connections.size] }
        }
    }


    /**
     * Changes the AISpeed in the [CableCar] class.
     */
    fun setAISpeed(speed: Int) {
        rootService.cableCar.AISpeed = speed
    }
}
