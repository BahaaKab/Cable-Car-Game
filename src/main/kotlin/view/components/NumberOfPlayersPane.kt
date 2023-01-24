package view.components

import tools.aqua.bgw.components.layoutviews.Pane
import tools.aqua.bgw.components.uicomponents.Button
import tools.aqua.bgw.components.uicomponents.Label
import tools.aqua.bgw.components.uicomponents.UIComponent
import tools.aqua.bgw.util.Font
import tools.aqua.bgw.visual.ColorVisual
import view.*

/** A pane which contains the display of the number of players in LobbyScene. Width: 125, Height: 50
 *
 * Default width: 120, default height: 66
 *
 * @param posX Horizontal coordinate for this Pane. Default: 0.
 * @param posY Vertical coordinate for this Pane. Default: 0.
 * @param playerNumber The number of players it should show.
 *  */
class NumberOfPlayersPane(posX: Number = 0, posY: Number = 0, val playerNumber: Int) :
    Pane<UIComponent>(posX, posY, width = 120, height = 66) {

    private val lineLabel = Label(
        posX = 0, posY = 15,
        width = 100, height = 4,
        font = Font(size = 1),
        visual = ColorVisual(233, 233, 236)
    )

    private val playersLabel = Label(
        posX = 48, posY = 48,
        width = 65, height = 18,
        text = "$playerNumber Players",
        font = Font(size = 14, color = DEFAULT_BLUE, family = DEFAULT_FONT_MEDIUM)
    ).apply { rotation = 315.0 }

    private val circleButton = Button(
        posX = 95, posY = 0,
        width = 35, height = 20
    ).apply {
        componentStyle = "-fx-background-radius: 100;-fx-background-color: rgba(255,255,255,1);" +
                "-fx-border-color: rgba(5, 24, 156, 1); -fx-border-radius: 100; -fx-border-width: 4;"
        scale = 0.69
        onMouseClicked = {
            CableCarApplication.lobbyScenes.forEach { it.displayPlayers(playerNumber) }
        }
    }


    init {
        if (playerNumber !in 3..6) {
            lineLabel.isVisible = false
        }
        addAll(lineLabel, circleButton, playersLabel)
    }

    fun blueLine() {
        lineLabel.visual = DEFAULT_BLUE_VISUAL
    }

    fun greyLine() {
        lineLabel.visual = ColorVisual(233, 233, 236)
    }

}