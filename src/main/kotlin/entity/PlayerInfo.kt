package entity

/**
 * This class contains the information of the Playerobjects.
 *
 * @property name of the Player
 *
 * @property playerType is either AI (easy or hard), HUMAN or NETWORK
 * While an easy AI player plays by doing random moves, the hard AI actually tries to win.
 * A Human player  is a player using this computer while a NETWORK player is someone who joines in our session
 * over the networks.
 *
 * @property color of the Player
 */
data class PlayerInfo(
    val name: String,
    var playerType: PlayerType,
    var color: Color
)