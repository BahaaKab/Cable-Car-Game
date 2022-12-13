package service

import entity.Color
import entity.PlayerInfo
import entity.PlayerType
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

/**
 * This class is used to test the Entity class [PlayerInfo]
 */
class PlayerInfoTest {

    /**
     * @property player1 is used as property to test if the entitylayer is working properly for the PlayerInfo
     */
    var player1 : PlayerInfo = PlayerInfo("TestName",PlayerType.AI_HARD , Color.BLUE)

    /**
     * First we assign some values for player1 and check if they have been assigned properly.
     */
    @Test
    fun constructorTest() {
        //player1.name = "TestName"
        player1.color = Color.YELLOW
        player1.playerType = PlayerType.HUMAN
        assertEquals("TestName", player1.name)
        assertEquals(Color.YELLOW, player1.color)
        assertEquals(PlayerType.HUMAN, player1.playerType)
    }
}