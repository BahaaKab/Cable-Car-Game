package entity

import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * Tests for the [CableCar] class.
 */
class CableCarTest {
    /**
     * Test the [CableCar] constructor.
     */
    @Test
    fun testConstructor() {
        val allowTileRotation = true
        val aiSpeed = 1
        val isHostPlayer = false
        val gameMode = GameMode.HOTSEAT
        val history = History()

        // Create a state
        val drawPile = List(10) { index ->
            GameTile(index, List(8) { it }.shuffled())
        }.toMutableList()

        val board = Array<Array<Tile?>>(10) {
            Array(10) { null }
        }

        val players = List(6) { Player(PlayerType.HUMAN, Color.BLUE, it.toString(), listOf()) }
        val activePlayer = players[0]
        val currentState = State(drawPile, activePlayer, board, players)

        val cableCar = CableCar(allowTileRotation, aiSpeed, isHostPlayer, gameMode, history, currentState)
        assertEquals(allowTileRotation, cableCar.allowTileRotation)
        assertEquals(aiSpeed, cableCar.AISpeed)
        assertEquals(isHostPlayer, cableCar.isHostPlayer)
        assertEquals(gameMode, cableCar.gameMode)
        assertEquals(history, cableCar.history)
    }
}