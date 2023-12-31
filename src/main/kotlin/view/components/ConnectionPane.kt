package view.components

import tools.aqua.bgw.components.layoutviews.Pane
import tools.aqua.bgw.components.uicomponents.Label
import tools.aqua.bgw.components.uicomponents.TextField
import tools.aqua.bgw.components.uicomponents.UIComponent
import tools.aqua.bgw.util.Font
import tools.aqua.bgw.visual.ColorVisual
import view.*

/** A pane to show Input-components in the connection Scene
 *
 * @param posX the x-position Pane
 * @param posY the y-position Pane */
class ConnectionPane(posX: Int, posY: Int) :
    Pane<UIComponent>(posX, posY, 850, 240) {

    private val playerName = TextField(
        width = 420, height = 50,
        posX = 760, posY = 350,
        font = Font(size = 20, color = DEFAULT_BLUE, family = DEFAULT_FONT_MEDIUM),
    ).apply {
        componentStyle =
            "-fx-background-color: rgba(0,0,0,0); -fx-border-color: rgb(5,24,156); -fx-border-width: 0 0 3 0"
    }

    private val sessionIDInput = TextField(
        width = 420, height = 50,
        posX = 760, posY = 435,
        font = Font(size = 20, color = DEFAULT_BLUE, family = DEFAULT_FONT_MEDIUM),
        text = "AMIN4PRESIDENT"
    ).apply {
        componentStyle =
            "-fx-background-color: rgba(0,0,0,0); -fx-border-color: rgb(5,24,156); -fx-border-width: 0 0 3 0"
    }

    init {
        addAll(

            Label(width = 850, height = 240, posX = 535, posY = 350).apply {
                componentStyle = "-fx-effect: dropshadow(one-pass-box, rgba(0,0,0,0.2), 10, 0, -1, 2);" +
                        "-fx-background-radius: 10;-fx-background-color: rgba(255,255,255,1);"
            },

            Label(
                posX = 555, posY = 388,
                width = 70, height = 10,
                visual = ColorVisual(230, 230, 233)
            ),
            Label(
                width = 300, height = 50,
                posX = 535, posY = 365,
                text = "Player:",
                font = Font(size = 20, color = DEFAULT_BLUE, family = DEFAULT_FONT_BOLD)
            ),
            Label(
                width = 300, height = 50,
                posX = 553, posY = 450,
                text = "Session ID:",
                font = Font(size = 20, color = DEFAULT_BLUE, family = DEFAULT_FONT_BOLD)
            )
        )

        addAll(playerName, sessionIDInput)
    }

    /** Returns the playerName typed in by the player */
    fun getPlayerName(): String {
        return playerName.text
    }

    /** Returns the SessionID typed in by the player */
    fun getSessionID(): String {
        return sessionIDInput.text
    }
}