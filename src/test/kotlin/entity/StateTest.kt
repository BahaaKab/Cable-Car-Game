package entity

import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * Tests for the [State] class.
 */
class StateTest {
    /**
     * Test the constructor of the [State] class.
     */
    @Test
    fun testConstructor() {
        val drawPile = List(10) { index ->
            GameTile(index, List(8) { it }.shuffled())
        }.toMutableList()

        val board = Array<Array<Tile?>>(10) {
            Array(10) { null }
        }

        val players = List(6) { Player(PlayerType.HUMAN, Color.BLUE, it.toString(), listOf()) }
        val activePlayer = players[0]
        val state = State(drawPile, activePlayer, board, players)

        assertEquals(drawPile, state.drawPile)
        assertEquals(board, state.board)
        assertEquals(players, state.players)
        assertEquals(activePlayer, state.activePlayer)
    }
}