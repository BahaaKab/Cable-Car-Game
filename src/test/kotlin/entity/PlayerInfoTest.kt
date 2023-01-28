package entity

import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * This class is used to test the Entity class [PlayerInfo]
 */
class PlayerInfoTest {
    /**
     * First we assign some values for player1 and check if they have been assigned properly.
     */
    @Test
    fun testConstructor() {
        val playerInfo = PlayerInfo("TestName", PlayerType.AI_HARD, Color.BLUE, isNetworkPlayer = true)
        assertEquals("TestName", playerInfo.name)
        assertEquals(Color.BLUE, playerInfo.color)
        assertEquals(PlayerType.AI_HARD, playerInfo.playerType)
        assertEquals(true, playerInfo.isNetworkPlayer)
    }


}