package entity

import edu.udo.cs.sopra.ntf.PlayerInfo

/**
 * This class contains the information of the player objects.
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
    fun toNetworkPlayerInfo() = PlayerInfo(name)
}