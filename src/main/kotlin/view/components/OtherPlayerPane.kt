package view.components

import tools.aqua.bgw.components.ComponentView
import tools.aqua.bgw.components.container.LinearLayout
import tools.aqua.bgw.components.gamecomponentviews.CardView
import tools.aqua.bgw.components.layoutviews.Pane
import tools.aqua.bgw.components.uicomponents.Label
import tools.aqua.bgw.core.Alignment
import tools.aqua.bgw.util.Font
import view.*

/**
 * The Pane which contains the other players' tiles, color, and score.
 *
 * Default width: 100, default height: 100.
 *
 * @param posX Horizontal coordinate for this Pane. Default: 0.
 * @param posY Vertical coordinate for this Pane. Default: 0.
 */
class OtherPlayerPane(posX: Number = 0, posY: Number = 0) :
    Pane<ComponentView>(posX = posX, posY = posY, width = 100, height = 100), Refreshable {

    var playerCard = LinearLayout<CardView>(
        posX = 0, posY = 0,
        width = 100, height = 100
    )

    val playerColorLabel = Label(
        posX = 0, posY = 122,
        width = 23, height = 6
    )

    val playerNameLabel = Label(
        posX = 30, posY = 116,
        width = 70, height = 12,
        text = "Player 2",
        font = Font(size = 14, color = java.awt.Color(5, 24, 156), family = DEFAULT_FONT_MEDIUM),
        alignment = Alignment.CENTER_LEFT
    )

    val playerScoreLabel = Label(
        posX = 30, posY = 137,
        width = 70, height = 12,
        text = "Score: 0",
        font = Font(size = 14, color = java.awt.Color(5, 24, 156), family = DEFAULT_FONT_MEDIUM),
        alignment = Alignment.CENTER_LEFT
    )

    init {
        addAll(playerCard, playerColorLabel, playerNameLabel, playerScoreLabel)
    }
}