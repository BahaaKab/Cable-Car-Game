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
 * Default width: 627, default height: 149.
 *
 * @param posX Horizontal coordinate for this Pane. Default: 0.
 * @param posY Vertical coordinate for this Pane. Default: 0.
 */
class OtherPlayersPane(posX: Number = 0, posY: Number = 0) :
    Pane<ComponentView>(posX = posX, posY = posY, width = 627, height = 149) {

    private val tile1Visual = ImageVisual(TILEIMAGELOADER.frontImageFor(21))
    private val tile2Visual = ImageVisual(TILEIMAGELOADER.frontImageFor(10))
    private val tile3Visual = ImageVisual(TILEIMAGELOADER.frontImageFor(59))
    private val tile4Visual = ImageVisual(TILEIMAGELOADER.frontImageFor(34))
    private val tile5Visual = ImageVisual(TILEIMAGELOADER.frontImageFor(49))

    private val otherPlayer1CardView = CardView(
        posX = 0, posY = 0,
        width = 100, height = 100,
        front = tile1Visual
    )

    private val otherPlayer2CardView = CardView(
        posX = 130, posY = 0,
        width = 100, height = 100,
        front = tile2Visual
    )

    private val otherPlayer3CardView = CardView(
        posX = 260, posY = 0,
        width = 100, height = 100,
        front = tile3Visual
    )

    private val otherPlayer4CardView = CardView(
        posX = 390, posY = 0,
        width = 100, height = 100,
        front = tile4Visual
    )

    private val otherPlayer5CardView = CardView(
        posX = 520, posY = 0,
        width = 100, height = 100,
        front = tile5Visual
    )

    private val otherPlayer1ColorLabel = Label(
        posX = 0, posY = 122,
        width = 30, height = 6,
        visual = DEFAULT_BLUE_COLOR
    )

    private val otherPlayer1NameLabel = Label(
        posX = 47, posY = 116,
        width = 60, height = 12,
        text = "Player 2",
        font = Font(size = 14, color = Color(5, 24, 156), family = DEFAULT_FONT_MEDIUM),
        alignment = Alignment.CENTER_LEFT
    )

    private val otherPlayer2ColorLabel = Label(
        posX = 130, posY = 122,
        width = 30, height = 6,
        visual = DEFAULT_RED_COLOR
    )

    private val otherPlayer2NameLabel = Label(
        posX = 177, posY = 116,
        width = 60, height = 12,
        text = "Player 3",
        font = Font(size = 14, color = Color(5, 24, 156), family = DEFAULT_FONT_MEDIUM),
        alignment = Alignment.CENTER_LEFT
    )

    private val otherPlayer3ColorLabel = Label(
        posX = 260, posY = 122,
        width = 30, height = 6,
        visual = DEFAULT_GREEN_COLOR
    )

    private val otherPlayer3NameLabel = Label(
        posX = 307, posY = 116,
        width = 60, height = 12,
        text = "Player 4",
        font = Font(size = 14, color = Color(5, 24, 156), family = DEFAULT_FONT_MEDIUM),
        alignment = Alignment.CENTER_LEFT
    )

    private val otherPlayer4ColorLabel = Label(
        posX = 390, posY = 122,
        width = 30, height = 6,
        visual = DEFAULT_PURPLE_COLOR
    )

    private val otherPlayer4NameLabel = Label(
        posX = 437, posY = 116,
        width = 60, height = 12,
        text = "Player 5",
        font = Font(size = 14, color = Color(5, 24, 156), family = DEFAULT_FONT_MEDIUM),
        alignment = Alignment.CENTER_LEFT
    )

    private val otherPlayer5ColorLabel = Label(
        posX = 520, posY = 122,
        width = 30, height = 6,
        visual = ColorVisual.BLACK
    )

    private val otherPlayer5NameLabel = Label(
        posX = 567, posY = 116,
        width = 60, height = 12,
        text = "Player 6",
        font = Font(size = 14, color = Color(5, 24, 156), family = DEFAULT_FONT_MEDIUM),
        alignment = Alignment.CENTER_LEFT
    )

    private val otherPlayer1ScoreLabel = Label(
        posX = 47, posY = 137,
        width = 60, height = 12,
        text = "Score: 0",
        font = Font(size = 14, color = Color(5, 24, 156), family = DEFAULT_FONT_MEDIUM),
        alignment = Alignment.CENTER_LEFT
    )

    private val otherPlayer2ScoreLabel = Label(
        posX = 177, posY = 137,
        width = 60, height = 12,
        text = "Score: 0",
        font = Font(size = 14, color = Color(5, 24, 156), family = DEFAULT_FONT_MEDIUM),
        alignment = Alignment.CENTER_LEFT
    )

    private val otherPlayer3ScoreLabel = Label(
        posX = 307, posY = 137,
        width = 60, height = 12,
        text = "Score: 3",
        font = Font(size = 14, color = Color(5, 24, 156), family = DEFAULT_FONT_MEDIUM),
        alignment = Alignment.CENTER_LEFT
    )

    private val otherPlayer4ScoreLabel = Label(
        posX = 437, posY = 137,
        width = 60, height = 12,
        text = "Score: 2",
        font = Font(size = 14, color = Color(5, 24, 156), family = DEFAULT_FONT_MEDIUM),
        alignment = Alignment.CENTER_LEFT
    )

    private val otherPlayer5ScoreLabel = Label(
        posX = 567, posY = 137,
        width = 60, height = 12,
        text = "Score: 0",
        font = Font(size = 14, color = Color(5, 24, 156), family = DEFAULT_FONT_MEDIUM),
        alignment = Alignment.CENTER_LEFT
    )

    init {
        addAll(
            otherPlayer1CardView, otherPlayer1ColorLabel, otherPlayer1NameLabel, otherPlayer1ScoreLabel,
            otherPlayer2CardView, otherPlayer2ColorLabel, otherPlayer2NameLabel, otherPlayer2ScoreLabel,
            otherPlayer3CardView, otherPlayer3ColorLabel, otherPlayer3NameLabel, otherPlayer3ScoreLabel,
            otherPlayer4CardView, otherPlayer4ColorLabel, otherPlayer4NameLabel, otherPlayer4ScoreLabel,
            otherPlayer5CardView, otherPlayer5ColorLabel, otherPlayer5NameLabel, otherPlayer5ScoreLabel,
        )
    }
}