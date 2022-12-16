package entity

/**
 * The CableCar entity is used to manage the game.
 * It also connects the entity layer with the [RootService] on the service layer.
 *
 * @property allowTileRotation: true allows us to rotate tiles; false disables the tile rotation feature
 * @property AISpeed: sets the speed of the AI and is changeable during the game
 * @property isHostPlayer: Indicates if the current player is a host player or not
 * @property gameMode: Are we playing in hot seat mode or online over the internet?
 * @property history: Saves other [State] objects
 * @property currentState: Is the [State] Object which is currently the active game
 */
class CableCar (
    val allowTileRotation : Boolean,
    var AISpeed : Int,
    val isHostPlayer : Boolean,
    val gameMode : GameMode,
    var history : History,
    var currentState : State)