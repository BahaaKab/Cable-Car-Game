package service
import entity.*
import org.junit.jupiter.api.assertThrows
import kotlin.test.*

/**
 * This class has the purpose to test the [PlayerActionService] Class
 * **/
class PlayerActionServiceTest {

    //Creates a new game instance
    var rootService = RootService()
    var setup = rootService.setupService
    var player1 = PlayerInfo("Marie", PlayerType.HUMAN, Color.YELLOW, false)
    var player2 = PlayerInfo("Larissa", PlayerType.HUMAN, Color.BLUE, false)
    val players = listOf<PlayerInfo>(player1, player2)


    /**
     * Tests the undo function
     *
     * First we check that the undo stack and redo stack are empty
     * Afterwards we do a player move and check if the undo states are not empty anymore
     * After the undo we have to test if the undo stack is empty and the redo stack is not
     * **/
    @Test
    fun testUndo(){
        setup.startLocalGame(players, false, 0)
        val game = rootService.cableCar.currentState
        val history = rootService.cableCar.history
        //Tests if the initialization works
        assertEquals(history.redoStates.size, 0)
        assertEquals(history.undoStates.size, 0)
        //Executes a player action
        rootService.playerActionService.placeTile(1,3)
        assertNotNull(game.board[1][3])
        //Tests the undo function
        assertEquals(history.undoStates.size,1)
        rootService.playerActionService.undo()
        assertNull(game.board[1][3])
        assertEquals(history.redoStates.size, 1)
    }

    /**
     * Tests the redo function
     *
     * We undo a state and then we redo it again
     * **/
    @Test
    fun testRedo(){
        setup.startLocalGame(players, false, 0)
        val game = rootService.cableCar.currentState
        val history = rootService.cableCar.history
        //Tests if the initialization works
        assertEquals(history.redoStates.size, 0)
        assertEquals(history.undoStates.size, 0)
        //Executes a player action
        rootService.playerActionService.placeTile(1,3)
        assertNotNull(game.board[1][3])
        //Tests the undo function
        assertEquals(history.undoStates.size,1)
        rootService.playerActionService.undo()
        assertNull(game.board[1][3])
        assertEquals(history.redoStates.size, 1)
        rootService.playerActionService.redo()
        assertEquals(history.redoStates.size, 0)
        assertEquals(history.undoStates.size,1)
        assertNotNull(game.board[1][3])

    }



    /**
     * Tests the drawPile function
     *
     * First we draw and play some Tiles.
     * After that we check if we can draw Tiles from an empty drawPile
     * **/
    @Test
    fun testDrawPile(){
        setup.startLocalGame(players, false, 0)
        val game = rootService.cableCar.currentState
        assertEquals(58,game.drawPile.size)

        //Draws Tiles
        rootService.playerActionService.drawTile()
        assertEquals(57, game.drawPile.size)
        rootService.playerActionService.placeTile(1,5)
        assertNotNull(game.board[1][5])

        rootService.playerActionService.drawTile()
        assertEquals(56, game.drawPile.size)
        rootService.playerActionService.placeTile(1,3)
        assertNotNull(game.board[1][3])

        rootService.playerActionService.drawTile()
        assertEquals(55, game.drawPile.size)
        rootService.playerActionService.placeTile(1,4)
        assertNotNull(game.board[1][4])

        //Tests if we can draw from an empty draw pile
        game.drawPile.removeAll(game.drawPile)
        assertEquals(0, game.drawPile.size)
        assertThrows<Exception> { rootService.playerActionService.drawTile() }


    }

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
    fun testPositionIsIllegal1() {
        setup.startLocalGame(players, false, 0)
        assertNotNull(rootService.cableCar)
        val game = rootService.cableCar.currentState

        val gameTile = GameTile(1, listOf(7,2,1,4,3,6,5,0))
        assertEquals(true, rootService.playerActionService.positionIsIllegal(1,1,gameTile))
        assertEquals(true, rootService.playerActionService.positionIsIllegal(1,8,gameTile))
        assertEquals(true, rootService.playerActionService.positionIsIllegal(8,1,gameTile))
        assertEquals(true, rootService.playerActionService.positionIsIllegal(8,8,gameTile))
    }

    /**
     * Test if illegal positions get legal if no other legal position is possible
     */
    @Test
    fun testPositionIsIllegal2() {
        setup.startLocalGame(players, false, 0)
        assertNotNull(rootService.cableCar)
        val game = rootService.cableCar.currentState
        // All other Grid-Positions except the corners get a GameTile
        for(i in 0..9){
            for(j in 0..9){
                if(game.board[i][j] != null && !(i==1 && j==1) && !(i==1 && j==8) && !(i==8 && j==8)
                    && !(i==4 && j==4) && !(i==4 && j==5) && !(i==5 && j==4) && !(i==5 && j==5)){
                    game.board[i][j] = game.drawPile.removeFirst()
                }
            }
        }
        val gameTile = GameTile(1, listOf(7,2,1,4,3,6,5,0))
        // All corner positions should be legal now
        assertEquals(false, rootService.playerActionService.positionIsIllegal(1,1,gameTile))
        assertEquals(false, rootService.playerActionService.positionIsIllegal(1,8,gameTile))
        assertEquals(false, rootService.playerActionService.positionIsIllegal(8,1,gameTile))
        assertEquals(false, rootService.playerActionService.positionIsIllegal(8,8,gameTile))
    }
}