package entity

/**
 * Enum to distinguish between the six possible colors:
 * yellow, blue, orange, green, purple, black
 */
enum class Color {
    YELLOW,
    BLUE,
    ORANGE,
    GREEN,
    PURPLE,
    BLACK,
    ;

    /**
     * Provide a string to represent the color of the player.
     */
    override fun toString() = when (this) {
        YELLOW -> "yellow"
        BLUE -> "blue"
        ORANGE -> "orange"
        GREEN -> "green"
        PURPLE -> "purple"
        BLACK -> "black"
    }

}