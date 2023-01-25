package service

import edu.udo.cs.sopra.ntf.TileInfo
import entity.*

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


        if (!isPlaceable(tileToPlace, posX, posY, cableCar.allowTileRotation)) {
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


    fun isPlaceable(tile: GameTile, x: Int, y: Int, rotationAllowed: Boolean) =
        Pair(x, y) in getPlaceablePositions(tile, rotationAllowed)


    /**
     * Get all positions, where it is allowed to place a given tile.
     *
     * @param tile The given tile
     *
     * @return All positions, where the placement is valid and would not produce a path of length one
     */
    fun getPlaceablePositions(tile: GameTile, rotationAllowed: Boolean): Set<Pair<Int, Int>> {
        val validPositions = getValidPositions()

        if (!rotationAllowed) {
            val onePointPositions = getOnePointPositions(tile)
            return validPositions.minus(onePointPositions)
        }

        val rotatedTiles = List(4) { i ->
            val tileToRotate = tile.deepCopy()
            repeat(i) { tileToRotate.rotate(true) }
            tileToRotate
        }

        val placeablePositionsForEachRotation = rotatedTiles.map { validPositions.minus(getOnePointPositions(it)) }
        // If there are not placeable positions at all, considering all rotations, ignore the one point rule
        return if (placeablePositionsForEachRotation.all { it.isEmpty() }) {
            validPositions
        } else {
            // Otherwise return the placeable positions of the current rotation, although they might be zero
            placeablePositionsForEachRotation.first()
        }


    }

    /**
     * Get all positions, that produce a path of length one for a given tile
     *
     * @param tile The given tile
     *
     * @return All positions, that produce a  path of length one for the tile
     */
    fun getOnePointPositions(tile: GameTile): Set<Pair<Int, Int>> {
        var onePointPositions = setOf<Pair<Int, Int>>()
        // Only positions adjacent to the station tiles can build one point paths
        val topStationTileNeighbours = List(8) { x -> Pair(x + 1, 1) }
        val rightStationTileNeighbours = List(8) { y -> Pair(8, y + 1) }
        val bottomStationTileNeighbours = List(8) { x -> Pair(x + 1, 8) }
        val leftStationTileNeighbours = List(8) { y -> Pair(1, y + 1) }
        // One way to get a one point path is to connect a station tile with itself
        if (tile.connections[0] == 1) {
            onePointPositions = onePointPositions.plus(topStationTileNeighbours)
        }
        if (tile.connections[2] == 3) {
            onePointPositions = onePointPositions.plus(rightStationTileNeighbours)
        }
        if (tile.connections[4] == 5) {
            onePointPositions = onePointPositions.plus(bottomStationTileNeighbours)
        }
        if (tile.connections[6] == 7) {
            onePointPositions = onePointPositions.plus(leftStationTileNeighbours)
        }
        // The other way to get a one point path is a edge loop
        if (tile.connections[0] == 7 || tile.connections[1] == 6) {
            onePointPositions = onePointPositions.plus(Pair(1, 1))
        }
        if (tile.connections[2] == 1 || tile.connections[3] == 0) {
            onePointPositions = onePointPositions.plus(Pair(8, 1))
        }
        if (tile.connections[4] == 3 || tile.connections[5] == 2) {
            onePointPositions = onePointPositions.plus(Pair(8, 8))
        }
        if (tile.connections[6] == 5 || tile.connections[7] == 4) {
            onePointPositions = onePointPositions.plus(Pair(1, 8))
        }

        return onePointPositions
    }

    /**
     * Get all positions, where it is valid to place a tile, meaning where a tile will have a neighbour. This does
     * not check, if the tile placement is also allowed with respect to the one point rule.
     *
     * @return All positions, where placement in general is valid
     */
    fun getValidPositions(): Set<Pair<Int, Int>> {
        // Build a set of positions that are adjacent to a station tile
        val validPositions = mutableSetOf<Pair<Int, Int>>()
        // Build a set of positions that are already occupied
        val alreadyOccupiedPositions = mutableSetOf(Pair(4, 4), Pair(4, 5), Pair(5, 4), Pair(5, 5))

        // Add all positions next to a station tile to the valid positions
        for (i in 1..8) {
            validPositions += setOf(Pair(1, i), Pair(8, i), Pair(i, 1), Pair(i, 8))
        }
        // In the 3, 5 and 6 player configuration, the station tiles in the bottom right corner are left out to
        // guarantee, that every player has the same amount of stations
        when (rootService.cableCar.currentState.players.size) {
            3, 5, 6 -> validPositions -= Pair(8, 8)
        }
        // The station tiles themself are already occupied
        for (i in 0..9) {
            alreadyOccupiedPositions += setOf(Pair(0, i), Pair(9, i), Pair(i, 0), Pair(i, 9))
        }

        // For each placed tile
        rootService.cableCar.currentState.placedTiles.forEach {
            // Add its neighbour positions to the valid positions as they are adjacent to the placed tile
            validPositions += setOf(
                Pair(it.x + 1, it.y),
                Pair(it.x - 1, it.y),
                Pair(it.x, it.y + 1),
                Pair(it.x, it.y - 1)
            )

            // Add the positions of the placed tiles themself to the occupied positions
            alreadyOccupiedPositions += Pair(it.x, it.y)
        }

        // Return only the valid positions that are not already occupied
        return validPositions - alreadyOccupiedPositions
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
        checkNotNull(tileToRotate).rotate(clockwise)
    }


    /**
     * Changes the AISpeed in the [CableCar] class.
     */
    fun setAISpeed(speed: Int) {
        rootService.cableCar.AISpeed = speed
    }
}
