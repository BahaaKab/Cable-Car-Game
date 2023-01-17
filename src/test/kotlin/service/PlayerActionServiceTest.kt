package service
import entity.*
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


    @Test
    fun testUndo(){
        //Diese Methode habe ich gemacht. Wir haben aber vereinbart, dass man gegentesten soll.
        //Daher muss jemand anderes diese Methode testen
    }

    @Test
    fun testRedo(){
        //Diese Methode habe ich gemacht. Wir haben aber vereinbart, dass man gegentesten soll.
        //Daher muss jemand anderes diese Methode testen
    }


    @Test
    fun testDrawPile(){
        //Diese Methode habe ich gemacht. Wir haben aber vereinbart, dass man gegentesten soll.
        //Daher muss jemand anderes diese Methode testen
    }

    /**
     * By creating a new game and then placing a tile we can test if the placing Method is working
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

    @Test
    fun testPositionIsIllegal() {
        //TBD

    }
}