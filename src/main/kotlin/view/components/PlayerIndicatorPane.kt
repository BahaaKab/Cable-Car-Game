package view.components

import tools.aqua.bgw.components.layoutviews.Pane
import tools.aqua.bgw.components.uicomponents.Label
import tools.aqua.bgw.components.uicomponents.UIComponent
import tools.aqua.bgw.core.Alignment
import tools.aqua.bgw.util.Font
import tools.aqua.bgw.visual.ColorVisual
import view.DEFAULT_BLUE
import view.DEFAULT_FONT_BOLD

/** A Pane that shows the indicators of a player (color & order) as labels
 *
 * Default width: 190, default height: 70
 *
 * @param posX Horizontal coordinate for this Pane. Default: 0.
 * @param posY Vertical coordinate for this Pane. Default: 0.
 * @param playerNumber The Number of the Player (e.g. 1 -> "Player1")
 * @param color The default color of the player (e.g. ColorVisual(1,1,1) )
 * */
class PlayerIndicatorPane(posX: Number = 0, posY: Number = 0, playerNumber: Int, color: ColorVisual) :
    Pane<UIComponent>(posX, posY, width = 190, height = 70) {

    private val colorLabel = Label(
        posX = 0, posY = 42,
        width = 75, height = 16,
        visual = color
    )

    private val playerLabel = Label(
        posX = 100, posY = 30,
        width = 90, height = 40,
        text = "Player $playerNumber:",
        alignment = Alignment.CENTER,
        font = Font(size = 21, color = DEFAULT_BLUE, family = DEFAULT_FONT_BOLD)
    )

    init {
        addAll(colorLabel, playerLabel)
    }
}