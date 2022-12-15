package entity

import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * This class is used to test the Entity class [History]
 */
class HistoryTest {

    /**
     * @property history0 is used to test the History entity class.
     */
    var history0: History = History()

    /**
     * The [History] does not have a constructor
     */
    @Test
    fun testConstructor() {
        //We cant test a constructor that is not existing
    }

    /**
     * By creating new state objects and pushing them to the stack
     * just to check the size of the stack, we can test if the history
     * works properly.
     */
    @Test
    fun historyTest() {
        //Create the first state
        val drawPile0: MutableList<GameTile> = mutableListOf()
        var activePlayer0: Player = Player(PlayerType.NETWORK, Color.YELLOW, "Laura", listOf())
        val board0 : Array<Array<Tile?>> = arrayOf()
        val state0: State = State(drawPile0, activePlayer0, board0)

        //Create a second state
        val drawPile1: MutableList<GameTile> = mutableListOf()
        var activePlayer1: Player = Player(PlayerType.NETWORK, Color.YELLOW, "Laura", listOf())
        val board1 : Array<Array<Tile?>> = arrayOf()
        val state1: State = State(drawPile1, activePlayer1, board1)

        //Create a third state
        val drawPile2: MutableList<GameTile> = mutableListOf()
        var activePlayer2: Player = Player(PlayerType.NETWORK, Color.YELLOW, "Laura", listOf())
        val board2 : Array<Array<Tile?>> = arrayOf()
        val state2: State = State(drawPile2, activePlayer2, board2)

        //After we have successfully created different states
        //We can now test the history.

        assertEquals(0,history0.redoStates.size)
        history0.redoStates.add(state0)
        assertEquals(1,history0.redoStates.size)
        assertEquals(0,history0.undoStates.size)
        history0.undoStates.add(state0)
        assertEquals(1, history0.undoStates.size)
        history0.redoStates.add(state1)
        assertEquals(2,history0.redoStates.size)
        history0.redoStates.add(state2)
        assertEquals(3,history0.redoStates.size)
        history0.clearRedos()
        assertEquals(0,history0.redoStates.size)
        history0.undoStates.add(state0)
        assertEquals(1, history0.undoStates.size)
        history0.clearRedos()
        assertEquals(0,history0.redoStates.size)


    }
}