package service

import entity.*
import kotlin.test.*

/**
 * This class has the purpose to test the [PlayerActionService] Class
 * **/
class PlayerActionServiceTest {

    //Creates a new game instance
    private var rootService = RootService()
    private var setup = rootService.setupService
    private var player1 = PlayerInfo("Marie", PlayerType.HUMAN, Color.YELLOW, false)
    private var player2 = PlayerInfo("Larissa", PlayerType.HUMAN, Color.BLUE, false)
    private val players = listOf(player1, player2)


    /**
     * Tests the undo redo function
     *
     * First we check that the undo stack and redo stack are empty
     * Afterwards we do some player moves and check if the undo states are not empty anymore
     * After the undo we have to test if the undo stack is empty and the redo stack is not
     * We undo a state and then we redo it again
     * **/
    @Test
    fun testUndoRedo() {
        //Initialize new game
        setup.startLocalGame(players, false, 0)
        var game = rootService.cableCar.currentState
        val history = rootService.cableCar.history
        //Tests if the initialization works
        assertEquals(0, history.redoStates.size)
        assertEquals(1, history.undoStates.size)
        //Executes a player action
        game.activePlayer.handTile = GameTile(1, listOf(5, 4, 7, 6, 1, 0, 3, 2))
        rootService.playerActionService.placeTile(1, 3)
        assertNotNull(game.board[1][3])
        //Executes another player action
        rootService.playerActionService.placeTile(2, 3)
        assertNotNull(game.board[2][3])
        //Tests the undo function
        assertEquals(3, history.undoStates.size)
        //Remove 2 states from the stack (because we have 2 players)
        rootService.playerActionService.undo()
        //After calling the undo function, we have to re-set the current state
        game = rootService.cableCar.currentState
        assertEquals(1, history.undoStates.size)
        assertNull(game.board[1][3])
        assertNull(game.board[2][3])
        assertEquals(2, history.redoStates.size)
        //Tests if the redo function works
        rootService.playerActionService.redo()
        game = rootService.cableCar.currentState
        assertEquals(0, history.redoStates.size)
        assertEquals(3, history.undoStates.size)
        assertNotNull(game.board[1][3])
    }

    /**
     *
     */
    @Test
    fun testIsPlaceable() {
        val rootService = RootService()
        val allowRotation = true
        rootService.setupService.startLocalGame(players, allowRotation, 0)

        val gameTile = GameTile(61, listOf(1, 0, 3, 2, 5, 4, 7, 6))

        val onePointPositions = rootService.playerActionService.getOnePointPositions(gameTile)
        assertTrue(Pair(1, 1) in onePointPositions)
        assertFalse(Pair(2, 4) in onePointPositions)
        val placeablePositions = rootService.playerActionService.getPlaceablePositions(gameTile, allowRotation)
        assertTrue(Pair(1, 5) in placeablePositions)
        assertFalse(Pair(2, 5) in placeablePositions)

        val otherGameTile = GameTile(61, listOf(5, 2, 1, 4, 3, 0, 7, 6))
        assertFalse(rootService.playerActionService.isPlaceable(otherGameTile, 1, 5, allowRotation))
    }


    /**
     * By creating a new game and then placing a tile we can test if the placing Method is working.
     * In this case a tile is placed on a legal position firstly and then on an invalid position because no adjacent
     * [StationTile] or [GameTile]
     * **/
    @Test
    fun testPlaceTile() {
        setup.startLocalGame(players, false, 0)
        assertNotNull(rootService.cableCar)
        val game = rootService.cableCar.currentState

        //Tests placing to a legal position
        assertNull(game.board[1][3])
        // Guarantee, that the tile is placeable at the given position
        game.activePlayer.handTile = GameTile(1, listOf(5, 4, 7, 6, 1, 0, 3, 2))
        rootService.playerActionService.placeTile(1, 3)
        assertNotNull(game.board[1][3])

        //Tests placing to an illegal position
        assertNull(game.board[6][6])
        rootService.playerActionService.placeTile(6, 6)
        assertNull(game.board[6][6])
    }

    /**
     * Test for checking if a path of length 1 gets constructed
     */
    @Test
    fun testPositionIsIllegal() {
        val rotationAllowed = false
        setup.startLocalGame(players, rotationAllowed, 0)

        val gameTile = GameTile(1, listOf(7, 2, 1, 4, 3, 6, 5, 0))
        val illegalPositions = setOf(Pair(1, 1), Pair(1, 8), Pair(8, 1), Pair(8, 8))
        illegalPositions.forEach { (x, y) ->
            assertFalse(rootService.playerActionService.isPlaceable(gameTile, x, y, rotationAllowed))
        }
    }

    /**
     * Tests if we can or cannot place illegal tiles
     */
    @Test
    fun testPlacingIllegalTile() {
        setup.startLocalGame(players, false, 0)
        val game = rootService.cableCar.currentState

        val gameTile = GameTile(1, listOf(7, 2, 1, 4, 3, 6, 5, 0))
        game.activePlayer.handTile = gameTile

        rootService.playerActionService.placeTile(1, 1)
        assertNull(game.board[1][1])
        rootService.playerActionService.placeTile(1, 8)
        assertNull(game.board[1][8])
        rootService.playerActionService.placeTile(8, 1)
        assertNull(game.board[8][1])
        rootService.playerActionService.placeTile(8, 8)
        assertNull(game.board[8][8])
    }

    /**
     * Test to check if there is a possibility to place the [GameTile] on a legal position or if there are only illegal
     * positions.
     * In this case there are legal positions left
     */
    @Test
    fun testLegalPositionsLeft() {
        val rotationAllowed = false
        setup.startLocalGame(players, rotationAllowed, 0)
        val gameTile = GameTile(1, listOf(7, 2, 1, 4, 3, 6, 5, 0))
        val legalPositions = rootService.playerActionService.getValidPositions() -
                rootService.playerActionService.getPlaceablePositions(gameTile, rotationAllowed)

        assertTrue(legalPositions.isNotEmpty())
    }


    /**
     * Tests if we can rotate tiles to the left when [CableCar.allowTileRotation] is true.
     * **/
    @Test
    fun testRotateTileLeftWhenRotationAllowed() {
        setup.startLocalGame(players, true, 0)
        val activePlayer = rootService.cableCar.currentState.activePlayer
        val gameTile = GameTile(1, listOf(7, 4, 3, 2, 1, 6, 5, 0))
        val expectedConnections = listOf(1, 0, 7, 4, 3, 6, 5, 2)

        activePlayer.handTile = gameTile
        rootService.playerActionService.rotateTileLeft()
        var currentConnections = activePlayer.handTile?.connections
        //Tests if the rotation works
        assertEquals(expectedConnections, currentConnections)
        rootService.playerActionService.rotateTileLeft()

        // Connections should not be the same after another rotation
        currentConnections = activePlayer.handTile?.connections
        assertNotEquals(currentConnections, listOf(1, 0, 7, 4, 3, 6, 5, 2))
    }

    /**
     * Tests if we can rotate tiles to the left although [CableCar.allowTileRotation] is false.
     * **/
    @Test
    fun testRotateTileLeftWhenRotationNotAllowed() {
        setup.startLocalGame(players, false, 0)
        val gameTile = GameTile(1, listOf(7, 4, 3, 2, 1, 6, 5, 0))
        val activePlayer = rootService.cableCar.currentState.activePlayer
        val expectedConnections = gameTile.connections.map { it }

        activePlayer.handTile = gameTile
        rootService.playerActionService.rotateTileLeft()
        val connections = activePlayer.handTile?.connections

        //Tests if the rotation works
        assertEquals(expectedConnections, connections)
    }

    /**
     * Tests if we can rotate tiles to the right when [CableCar.allowTileRotation] is true.
     * **/
    @Test
    fun testRotateTileRightWhenRotationAllowed() {
        setup.startLocalGame(players, true, 0)
        val activePlayer = rootService.cableCar.currentState.activePlayer
        val gameTile = GameTile(1, listOf(7, 4, 3, 2, 1, 6, 5, 0))
        val expectedConnections = listOf(7, 2, 1, 6, 5, 4, 3, 0)

        activePlayer.handTile = gameTile
        rootService.playerActionService.rotateTileRight()
        var currentConnections = activePlayer.handTile?.connections
        //Tests if the rotation works
        assertEquals(expectedConnections, currentConnections)
        rootService.playerActionService.rotateTileRight()

        // Connections should not be the same after another rotation
        currentConnections = activePlayer.handTile?.connections
        assertNotEquals(currentConnections, listOf(7, 2, 1, 6, 5, 4, 3, 0))
    }

    /**
     * Tests if we can rotate tiles to the left although [CableCar.allowTileRotation] is false.
     */
    @Test
    fun testRotateTileRightWhenRotationNotAllowed() {
        setup.startLocalGame(players, false, 0)
        val gameTile = GameTile(1, listOf(7, 4, 3, 2, 1, 6, 5, 0))
        val activePlayer = rootService.cableCar.currentState.activePlayer
        val expectedConnections = gameTile.connections.map { it }

        activePlayer.handTile = gameTile
        rootService.playerActionService.rotateTileRight()
        val connections = activePlayer.handTile?.connections

        //Tests if the rotation works
        assertEquals(expectedConnections, connections)
    }

    /**
     * Tests if we can set the AI Speed
     * **/
    @Test
    fun testSetAISpeed() {
        setup.startLocalGame(players, false, 0)
        assertEquals(0, rootService.cableCar.aiSpeed)
        rootService.playerActionService.setAISpeed(4)
        assertEquals(4, rootService.cableCar.aiSpeed)
    }

}