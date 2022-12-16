package entity

import BOT_LEFT
import BOT_RIGHT
import kotlin.test.*

class PlayerTest {

    // The Station Tiles we give to the players
    private val stationTiles = listOf(StationTile(listOf(BOT_RIGHT, BOT_LEFT)))

    private val newHandTile = GameTile(2, listOf(1,0,3,2,5,4,7,6))
    private val newCurrentTile = GameTile(18, listOf(7,6,5,4,3,2,1,0))

    // Construct two players
    private val player1 = Player(
        playerType = PlayerType.HUMAN,
        color = Color.YELLOW,
        name = "Tester1",
        stationTiles = stationTiles
    )

    private val player2 = Player(
        playerType = PlayerType.HUMAN,
        color = Color.YELLOW,
        name = "Tester2",
        stationTiles = stationTiles
    ).apply {
        handTile = newHandTile
        currentTile = newCurrentTile
    }

    @Test
    fun testConstructor() {

        // Check if the constructor assigned the correct values
        assertEquals(PlayerType.HUMAN, player1.playerType)
        assertEquals(Color.YELLOW, player1.color)
        assertEquals("Tester1", player1.name)
        assertEquals(stationTiles, player1.stationTiles)
        assertEquals(0, player1.score)
        assertEquals(null, player1.handTile)
        assertEquals(null, player1.currentTile)

        // Now we change the attributes of player
        player1.score = 21
        player1.handTile = newHandTile
        player1.currentTile = newCurrentTile

        // Check the changed attributes again
        assertEquals(21, player1.score)
        assertEquals(newHandTile, player1.handTile)
        assertEquals(newCurrentTile, player1.currentTile)
    }

    @Test
    fun testDeepCopy() {
        val copiedPlayer = player2.deepCopy()

        // Check that we create a new player object
        assertNotSame(player2, copiedPlayer)

        // Now we check that both have the same attribute values
        assertEquals(player2.playerType, copiedPlayer.playerType)
        assertEquals(player2.color, copiedPlayer.color)
        assertEquals(player2.name, copiedPlayer.name)
        assertEquals(player2.score, copiedPlayer.score)

        assertEquals(player2.handTile?.id, copiedPlayer.handTile?.id)
        assertEquals(player2.handTile?.connections, copiedPlayer.handTile?.connections)
        assertEquals(player2.handTile?.isEndTile, copiedPlayer.handTile?.isEndTile)
        assertEquals(player2.handTile?.isEmpty, copiedPlayer.handTile?.isEmpty)
        assertEquals(player2.handTile?.connectors, copiedPlayer.handTile?.connectors)

        assertEquals(player2.currentTile?.id, copiedPlayer.currentTile?.id)
        assertEquals(player2.currentTile?.connections, copiedPlayer.currentTile?.connections)
        assertEquals(player2.currentTile?.isEndTile, copiedPlayer.currentTile?.isEndTile)
        assertEquals(player2.currentTile?.isEmpty, copiedPlayer.currentTile?.isEmpty)
        assertEquals(player2.currentTile?.connectors, copiedPlayer.currentTile?.connectors)

        assertEquals(player2.stationTiles[0].isEmpty, copiedPlayer.stationTiles[0].isEmpty)
        assertEquals(player2.stationTiles[0].isEndTile, copiedPlayer.stationTiles[0].isEndTile)
        assertEquals(player2.stationTiles[0].connectors, copiedPlayer.stationTiles[0].connectors)
        assertEquals(player2.stationTiles[0].connections, copiedPlayer.stationTiles[0].connections)
        assertEquals(player2.stationTiles[0].startPosition, copiedPlayer.stationTiles[0].startPosition)

        // Save the old values
        val oldScore = player2.score
        val oldHandTileID = player2.handTile?.id
        val oldCurrentTileID = player2.currentTile?.id

        // Change the attributes in the copy
        copiedPlayer.score = 80
        copiedPlayer.handTile = GameTile(5, listOf(4,5,3,2,0,1,7,6))
        copiedPlayer.currentTile = GameTile(50, listOf(3,2,1,0,7,6,5,4))

        // Check if changing attributes in the copy affects the original (it shouldn't)
        assertEquals(oldScore, player2.score)

        // We only check the id because if it changed, everything else also changed
        assertEquals(oldHandTileID, player2.handTile?.id)
        assertEquals(oldCurrentTileID, player2.currentTile?.id)
    }
}