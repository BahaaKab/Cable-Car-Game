package entity

import java.util.*

/**
 * This class stores all the previous and undone states.
 *
 * @property undoStates All the previous states
 *
 * @property redoStates All states that were undone
 */
class History {

    val undoStates = Stack<State>()
    val redoStates = Stack<State>()

    /**
     * Clears the [redoStates] stack
     */
    fun clearRedos() = redoStates.clear()
}