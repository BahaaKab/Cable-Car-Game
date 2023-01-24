package entity

import edu.udo.cs.sopra.ntf.PlayerInfo

/**
 * This class contains the information of the Playerobjects.
 *
 * @property name of the Player
 * @property playerType Whether the player is either an AI_EASY, AI_HARD or a HUMAN
 * @property color of the Player
 * @property isNetworkPlayer Whether the player is joining over a network connection.
 */
data class PlayerInfo(
    val name: String,
    val playerType: PlayerType,
    val color: Color,
    val isNetworkPlayer: Boolean
) {
    /**
     * Convert the PlayerInfo into a Network PlayerInfo
     * **/
    fun toNetworkPlayerInfo() = PlayerInfo(name)
}