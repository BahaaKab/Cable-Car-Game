package entity

import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals

/**
 * This class is used to test the Entity class [History]
 */
class HistoryTest {

    /**
     * @property history is used to test the History entity class.
     */
    var history: History = History()

    /**
     * The [History] does not have a constructor.
     * Therefore, we just check if the stacks in the history are empty
     */
    @Test
    fun testConstructor() {
        assertEquals(history.redoStates.size, 0)
        assertEquals(history.undoStates.size, 0)
    }

    /**
     * By creating new state objects and pushing them to the stack
     * just to check the size of the stack and if it contains the given state
     * object, we can test if the history
     * works properly.
     */
    @Test
    fun testHistory() {
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

        assertEquals(0,history.redoStates.size)
        history.redoStates.add(state0)
        assertContains(history.redoStates, state0)
        assertEquals(1,history.redoStates.size)
        assertEquals(0,history.undoStates.size)
        history.undoStates.add(state0)
        assertContains(history.undoStates, state0)
        assertEquals(1, history.undoStates.size)
        history.redoStates.add(state1)
        assertContains(history.redoStates, state1)

        assertEquals(2,history.redoStates.size)
        history.redoStates.add(state2)
        assertContains(history.redoStates, state2)
        assertEquals(3,history.redoStates.size)
        history.clearRedos()
        assertEquals(0,history.redoStates.size)
        history.undoStates.add(state0)
        assertContains(history.undoStates, state0)
        assertEquals(1, history.undoStates.size)
        history.clearRedos()
        assertEquals(0,history.redoStates.size)


    }
}