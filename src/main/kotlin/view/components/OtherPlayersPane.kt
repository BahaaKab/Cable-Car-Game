package view.components

import service.TileImageLoader
import tools.aqua.bgw.components.ComponentView
import tools.aqua.bgw.components.gamecomponentviews.CardView
import tools.aqua.bgw.components.layoutviews.Pane
import tools.aqua.bgw.components.uicomponents.Label
import tools.aqua.bgw.core.Alignment
import tools.aqua.bgw.util.Font
import tools.aqua.bgw.visual.ColorVisual
import tools.aqua.bgw.visual.ImageVisual
import view.DEFAULT_FONT_MEDIUM
import java.awt.Color

class OtherPlayersPane(posX: Int, posY: Int) :
    Pane<ComponentView>(posX, posY, 0, 0) {

    private val tileImageLoader = TileImageLoader()

    private val tile1Visual = ImageVisual(tileImageLoader.frontImageFor(21))
    private val tile2Visual = ImageVisual(tileImageLoader.frontImageFor(10))
    private val tile3Visual = ImageVisual(tileImageLoader.frontImageFor(59))
    private val tile4Visual = ImageVisual(tileImageLoader.frontImageFor(34))
    private val tile5Visual = ImageVisual(tileImageLoader.frontImageFor(49))

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
                visual = ColorVisual(17,139,206)
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
                visual = ColorVisual(213,41,39)
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
                visual = ColorVisual(12,111,47)
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
                visual = ColorVisual(142,13,78)
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
                visual = ColorVisual(0,0,0)
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