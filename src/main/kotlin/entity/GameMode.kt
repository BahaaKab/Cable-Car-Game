package entity

/**
 * Enum to distinguish between the two possible game modes:
 * Either Hotseat or Network
 */
enum class GameMode {
    HOTSEAT,
    NETWORK
    ;

    /**
     * Provide a string to represent the [GameMode]
     */
    override fun toString() = when (this) {
        HOTSEAT -> "Hotseat"
        NETWORK -> "Network"
    }
}