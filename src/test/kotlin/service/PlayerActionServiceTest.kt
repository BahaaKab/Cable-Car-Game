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
     * By creating a new game and then placing a tile we can test if the placing Method is working.
     * In this case a tile is placed on a legal position firstly and then on an invalid position because no adjacent
     * [StationTile] or [GameTile]
     * **/
    @Test
    fun testPlaceTile(){
        setup.startLocalGame(players, false, 0)
        assertNotNull(rootService.cableCar)
        val game = rootService.cableCar.currentState

        //Tests placing to a legal position
        assertNull(game.board[1][3])
        rootService.playerActionService.placeTile(1,3)
        assertNotNull(game.board[1][3])

        //Tests placing to an illegal position
        assertNull(game.board[6][6])
        rootService.playerActionService.placeTile(6,6)
        assertNull(game.board[6][6])
    }

    /**
     * Test for checking if a path of length 1 gets constructed
     */
    @Test
    fun testPositionIsIllegal() {
        setup.startLocalGame(players, false, 0)
        assertNotNull(rootService.cableCar)

        val gameTile = GameTile(1, listOf(7,2,1,4,3,6,5,0))
        assertTrue(rootService.playerActionService.positionIsIllegal(1,1,gameTile))
        assertTrue(rootService.playerActionService.positionIsIllegal(1,8,gameTile))
        assertTrue(rootService.playerActionService.positionIsIllegal(8,1,gameTile))
        assertTrue(rootService.playerActionService.positionIsIllegal(8,8,gameTile))
    }

    /**
     * Tests if we can or cannot place illegal tiles
     * **/
    @Test
    fun testPlacingIllegalTile() {
        setup.startLocalGame(players, false, 0)
        assertNotNull(rootService.cableCar)
        val game = rootService.cableCar.currentState

        val gameTile = GameTile(1, listOf(7,2,1,4,3,6,5,0))
        game.activePlayer.handTile = gameTile

        rootService.playerActionService.placeTile(1,1)
        assertNull(game.board[1][1])
        rootService.playerActionService.placeTile(1,8)
        assertNull(game.board[1][8])
        rootService.playerActionService.placeTile(8,1)
        assertNull(game.board[8][1])
        rootService.playerActionService.placeTile(8,8)
        assertNull(game.board[8][8])
    }

    /**
     * Place a tile, that has legal position. Expect, that it can be placed then to any adjacent tile.
     */
    @Test
    fun testPlaceTileAlthoughIllegal() {
        setup.startLocalGame(players, false, 0)
        val activePlayer = rootService.cableCar.currentState.activePlayer
        // Create a game tile, that will make always a one-point-track at the start of the game
        activePlayer.handTile = GameTile(1, listOf(1, 0, 3, 2, 5, 4, 7, 6))
        // Due to the special rule this should be placeable anywhere adjacent to a station tile, but not anywhere else
        rootService.playerActionService.placeTile(2, 2)
        assertNull(rootService.cableCar.currentState.board[2][2])
        rootService.playerActionService.placeTile(1, 1)
        assertNotNull(rootService.cableCar.currentState.board[1][1])
    }

    /**
     * Test to check if there is a possibility to place the [GameTile] on a legal position or if there are only illegal
     * positions.
     * In this case there are legal positions left
     */
    @Test
    fun testOnlyIllegalPositionsWithLegalPositionsLeft() {
        setup.startLocalGame(players, false, 0)
        assertNotNull(rootService.cableCar)
        val gameTile = GameTile(1, listOf(7,2,1,4,3,6,5,0))

        assertEquals(false, rootService.playerActionService.onlyIllegalPositionsLeft(gameTile))
    }

    /**
     * Test to check if there are only illegal positions left. In this case there are only illegal positions left
     */
    @Test
    fun testOnlyIllegalPositionsWithNoLegalPositionsLeft() {
        setup.startLocalGame(players, false, 0)
        assertNotNull(rootService.cableCar)
        val game = rootService.cableCar.currentState
        val gameTile = GameTile(1, listOf(7,2,1,4,3,6,5,0))

        for(i in 0..9){
            for(j in 0..9){
                if(game.board[i][j] == null && !(i==1 && j==1) && !(i==1 && j==8) && !(i==8 && j==8)
                    && !(i==8 && j==1) && !(i==4 && j==4) && !(i==4 && j==5) && !(i==5 && j==4) && !(i==5 && j==5)){
                    // On each free grid spot place a GameTile
                    game.board[i][j] = GameTile(1, listOf(7,2,1,4,3,6,5,0))
                }
            }
        }
        assertEquals(true, rootService.playerActionService.onlyIllegalPositionsLeft(gameTile))
    }

    /**
     * Checks if we can place tile on places it is supposed to
     * and also checks if we cant place tiles on places that it is not supposed to
     * **/
    @Test
    fun testIsAdjacentToTiles(){
        setup.startLocalGame(players, false, 0)
        assertFalse(rootService.playerActionService.isAdjacentToTiles(3,3))
        assertTrue(rootService.playerActionService.isAdjacentToTiles(1,3))
    }

    /**
     * Tests if we can rotate tiles to the left when [CableCar.allowTileRotation] is true.
     * **/
    @Test
    fun testRotateTileLeftWhenRotationAllowed() {
        setup.startLocalGame(players, true, 0)
        val activePlayer = rootService.cableCar.currentState.activePlayer
        val gameTile = GameTile(1, listOf(7,4,3,2,1,6,5,0))
        val expectedConnections = listOf(1,0,7,4,3,6,5,2)
        val expectedRotation = 270

        activePlayer.handTile = gameTile
        rootService.playerActionService.rotateTileLeft()
        var currentConnections = activePlayer.handTile?.connections
        //Tests if the rotation works
        assertEquals(expectedConnections, currentConnections)
        assertEquals(expectedRotation, activePlayer.handTile?.rotation)

        rootService.playerActionService.rotateTileLeft()

        // Connections should not be the same after another rotation
        currentConnections = activePlayer.handTile?.connections
        assertNotEquals(currentConnections,listOf(1,0,7,4,3,6,5,2))
    }

    /**
     * Tests if we can rotate tiles to the left although [CableCar.allowTileRotation] is false.
     * **/
    @Test
    fun testRotateTileLeftWhenRotationNotAllowed(){
        setup.startLocalGame(players, false, 0)
        val gameTile = GameTile(1, listOf(7,4,3,2,1,6,5,0))
        val activePlayer = rootService.cableCar.currentState.activePlayer
        val expectedConnections = gameTile.connections.map { it }
        val expectedRotation = 0

        activePlayer.handTile = gameTile
        rootService.playerActionService.rotateTileLeft()
        val connections = activePlayer.handTile?.connections

        // Rotation should not do anything
        assertEquals(expectedConnections, connections)
        assertEquals(expectedRotation, activePlayer.handTile?.rotation)
    }

    /**
     * Tests if we can rotate tiles to the right when [CableCar.allowTileRotation] is true.
     * **/
    @Test
    fun testRotateTileRightWhenRotationAllowed () {
        setup.startLocalGame(players, true, 0)
        val activePlayer = rootService.cableCar.currentState.activePlayer
        val gameTile = GameTile(1, listOf(7,4,3,2,1,6,5,0))
        val expectedConnections = listOf(7,2,1,6,5,4,3,0)
        val expectedRotation = 90

        activePlayer.handTile = gameTile
        rootService.playerActionService.rotateTileRight()
        var currentConnections = activePlayer.handTile?.connections
        //Tests if the rotation works
        assertEquals(expectedConnections, currentConnections)
        assertEquals(expectedRotation, activePlayer.handTile?.rotation)
        rootService.playerActionService.rotateTileRight()

        // Connections should not be the same after another rotation
        currentConnections = activePlayer.handTile?.connections
        assertNotEquals(currentConnections, listOf(7,2,1,6,5,4,3,0))
    }

    /**
     * Tests if we can rotate tiles to the left although [CableCar.allowTileRotation] is false.
     */
    @Test
    fun testRotateTileRightWhenRotationNotAllowed(){
        setup.startLocalGame(players, false, 0)
        val gameTile = GameTile(1, listOf(7,4,3,2,1,6,5,0))
        val activePlayer = rootService.cableCar.currentState.activePlayer
        val expectedConnections = gameTile.connections.map { it }
        val expectedRotation = 0

        activePlayer.handTile = gameTile
        rootService.playerActionService.rotateTileRight()
        val connections = activePlayer.handTile?.connections

        // Rotation should not do anything
        assertEquals(expectedConnections, connections)
        assertEquals(expectedRotation, activePlayer.handTile?.rotation)
    }

    /**
     * Test various sequential tile rotations
     */
    @Test
    fun testMultipleRotations() {
        setup.startLocalGame(players, true, 0)
        val gameTile = rootService.cableCar.currentState.activePlayer.handTile
        val connections = gameTile?.connections?.map { it }
        // Rotation four times left should result in a tile that looks as without rotation
        repeat(4) { rootService.playerActionService.rotateTileLeft() }
        assertEquals(connections, gameTile?.connections)
        assertEquals(0, gameTile?.rotation)
        // Rotation four times right should result in a tile that looks as without rotation
        repeat(4) { rootService.playerActionService.rotateTileRight() }
        assertEquals(connections, gameTile?.connections)
        assertEquals(0, gameTile?.rotation)
        // Rotation eight times left should result in a tile that looks as without rotation
        repeat(8) { rootService.playerActionService.rotateTileLeft() }
        assertEquals(connections, gameTile?.connections)
        assertEquals(0, gameTile?.rotation)
        // Rotation eight times right should result in a tile that looks as without rotation
        repeat(8) { rootService.playerActionService.rotateTileRight() }
        assertEquals(connections, gameTile?.connections)
        assertEquals(0, gameTile?.rotation)
        // Rotation left and then right should result in a tile that looks as without rotation
        rootService.playerActionService.rotateTileLeft()
        rootService.playerActionService.rotateTileRight()
        assertEquals(connections, gameTile?.connections)
        assertEquals(0, gameTile?.rotation)
        // Rotation right and then left should result in a tile that looks as without rotation
        rootService.playerActionService.rotateTileRight()
        rootService.playerActionService.rotateTileLeft()
        assertEquals(connections, gameTile?.connections)
        assertEquals(0, gameTile?.rotation)
    }

    /**
     * Tests if we can set the AI Speed
     * **/
    @Test
    fun testSetAISpeed(){
        setup.startLocalGame(players, false, 0)
        assertEquals(0, rootService.cableCar.AISpeed)
        rootService.playerActionService.setAISpeed(4)
        assertEquals(4, rootService.cableCar.AISpeed)
    }
}
