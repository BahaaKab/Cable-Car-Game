package entity

/**
 * Enum to distinguish between the for [PlayerType]s:
 * AI easy, AI hard, human and network
 */
enum class PlayerType {
    AI_EASY,
    AI_HARD,
    HUMAN,
    ;

    /**
     * provide a string to represent the [PlayerType] of the player
     */
    override fun toString() = when(this) {
        AI_EASY -> "AI_easy"
        AI_HARD -> "AI_hard"
        HUMAN -> "human"
    }
}