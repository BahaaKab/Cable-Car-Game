package view.components

import tools.aqua.bgw.components.ComponentView
import tools.aqua.bgw.components.gamecomponentviews.CardView
import tools.aqua.bgw.components.layoutviews.Pane
import tools.aqua.bgw.components.uicomponents.Label
import tools.aqua.bgw.core.Alignment
import tools.aqua.bgw.util.Font
import tools.aqua.bgw.visual.ColorVisual
import tools.aqua.bgw.visual.ImageVisual
import view.*
import java.awt.Color

/**
 * The Pane which contains the other players' tiles, color, and score.
 *
 * @param posX Horizontal coordinate for this Pane. Default: 0.
 * @param posY Vertical coordinate for this Pane. Default: 0.
 */
class OtherPlayersPane(posX: Number = 0, posY: Number = 0) :
    Pane<ComponentView>(posX, posY, 0, 0) {

    private val tile1Visual = ImageVisual(TILEIMAGELOADER.frontImageFor(21))
    private val tile2Visual = ImageVisual(TILEIMAGELOADER.frontImageFor(10))
    private val tile3Visual = ImageVisual(TILEIMAGELOADER.frontImageFor(59))
    private val tile4Visual = ImageVisual(TILEIMAGELOADER.frontImageFor(34))
    private val tile5Visual = ImageVisual(TILEIMAGELOADER.frontImageFor(49))

    init {
        addAll(
            CardView(
                width = 100, height = 100,
                front = tile1Visual
            ),

            CardView(
                posX = 130, posY = 0,
                width = 100, height = 100,
                front = tile2Visual
            ),

            CardView(
                posX = 260, posY = 0,
                width = 100, height = 100,
                front = tile3Visual
            ),

            CardView(
                posX = 390, posY = 0,
                width = 100, height = 100,
                front = tile4Visual
            ),

            CardView(
                posX = 520, posY = 0,
                width = 100, height = 100,
                front = tile5Visual
            ),

            Label(
                posX = 0, posY = 122,
                width = 30, height = 6,
                visual = DEFAULT_BLUE_COLOR
            ),

            Label(
                posX = 47, posY = 116,
                width = 60, height = 12,
                text = "Player 2",
                font = Font(size = 14, color = Color(5, 24, 156), family = DEFAULT_FONT_MEDIUM),
                alignment = Alignment.CENTER_LEFT
            ),

            Label(
                posX = 130, posY = 122,
                width = 30, height = 6,
                visual = DEFAULT_RED_COLOR
            ),

            Label(
                posX = 177, posY = 116,
                width = 60, height = 12,
                text = "Player 3",
                font = Font(size = 14, color = Color(5, 24, 156), family = DEFAULT_FONT_MEDIUM),
                alignment = Alignment.CENTER_LEFT
            ),

            Label(
                posX = 260, posY = 122,
                width = 30, height = 6,
                visual = DEFAULT_GREEN_COLOR
            ),

            Label(
                posX = 307, posY = 116,
                width = 60, height = 12,
                text = "Player 4",
                font = Font(size = 14, color = Color(5, 24, 156), family = DEFAULT_FONT_MEDIUM),
                alignment = Alignment.CENTER_LEFT
            ),

            Label(
                posX = 390, posY = 122,
                width = 30, height = 6,
                visual = DEFAULT_PURPLE_COLOR
            ),

            Label(
                posX = 437, posY = 116,
                width = 60, height = 12,
                text = "Player 5",
                font = Font(size = 14, color = Color(5, 24, 156), family = DEFAULT_FONT_MEDIUM),
                alignment = Alignment.CENTER_LEFT
            ),

            Label(
                posX = 520, posY = 122,
                width = 30, height = 6,
                visual = ColorVisual.BLACK
            ),

            Label(
                posX = 567, posY = 116,
                width = 60, height = 12,
                text = "Player 6",
                font = Font(size = 14, color = Color(5, 24, 156), family = DEFAULT_FONT_MEDIUM),
                alignment = Alignment.CENTER_LEFT
            ),

            Label(
                posX = 47, posY = 137,
                width = 60, height = 12,
                text = "Score: 0",
                font = Font(size = 14, color = Color(5, 24, 156), family = DEFAULT_FONT_MEDIUM),
                alignment = Alignment.CENTER_LEFT
            ),

            Label(
                posX = 177, posY = 137,
                width = 60, height = 12,
                text = "Score: 0",
                font = Font(size = 14, color = Color(5, 24, 156), family = DEFAULT_FONT_MEDIUM),
                alignment = Alignment.CENTER_LEFT
            ),

            Label(
                posX = 307, posY = 137,
                width = 60, height = 12,
                text = "Score: 3",
                font = Font(size = 14, color = Color(5, 24, 156), family = DEFAULT_FONT_MEDIUM),
                alignment = Alignment.CENTER_LEFT
            ),

            Label(
                posX = 437, posY = 137,
                width = 60, height = 12,
                text = "Score: 2",
                font = Font(size = 14, color = Color(5, 24, 156), family = DEFAULT_FONT_MEDIUM),
                alignment = Alignment.CENTER_LEFT
            ),

            Label(
                posX = 567, posY = 137,
                width = 60, height = 12,
                text = "Score: 0",
                font = Font(size = 14, color = Color(5, 24, 156), family = DEFAULT_FONT_MEDIUM),
                alignment = Alignment.CENTER_LEFT
            )
        )
    }
}