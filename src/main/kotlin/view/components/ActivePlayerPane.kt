package view.components

import tools.aqua.bgw.components.ComponentView
import tools.aqua.bgw.components.gamecomponentviews.CardView
import tools.aqua.bgw.components.layoutviews.Pane
import tools.aqua.bgw.components.uicomponents.Button
import tools.aqua.bgw.components.uicomponents.Label
import tools.aqua.bgw.core.Alignment
import tools.aqua.bgw.util.Font
import tools.aqua.bgw.visual.ImageVisual
import tools.aqua.bgw.visual.Visual
import view.*
import java.awt.Color
import javax.imageio.ImageIO

/**
 * The pane which contains everything relevant for the [entity.State.activePlayer].
 *
 * Default width: 630, default height: 240.
 *
 * @param posX Horizontal coordinate for this Pane. Default: 0.
 * @param posY Vertical coordinate for this Pane. Default: 0.
 */
class ActivePlayerPane(posX: Number = 0, posY: Number = 0) :
    Pane<ComponentView>(posX = posX, posY = posY, width = 630, height = 240) {

    private val tileVisual = ImageVisual(TILEIMAGELOADER.frontImageFor(0))

    private val rotateLeftVisual = ImageVisual(
        ImageIO.read(GameScene::class.java.getResource("/rotateLeft.png"))
    )
    private val rotateRightVisual = ImageVisual(
        ImageIO.read(GameScene::class.java.getResource("/rotateRight.png"))
    )
    private val drawTileVisual = ImageVisual(ImageIO.read(GameScene::class.java.getResource("/drawTile.png")))

    private val activePlayerTile = CardView(
        posX = 390, posY = 0,
        width = 240, height = 240,
        front = tileVisual
    )

    private val activePlayerColorLabel = Label(
        posX = 10, posY = 26,
        width = 70, height = 10,
        visual = DEFAULT_YELLOW_COLOR
    )

    private val activePlayerNameLabel = Label(
        posX = 110, posY = 16,
        width = 100, height = 20,
        text = "Player 1",
        font = Font(
            size = 20,
            color = DEFAULT_BLUE,
            family = DEFAULT_FONT_MEDIUM
        ),
        alignment = Alignment.CENTER_LEFT
    )

    private val activePlayerScoreLabel = Label(
        posX = 110, posY = 41,
        width = 100, height = 20,
        text = "Score: 9",
        font = Font(
            size = 20,
            color = DEFAULT_BLUE,
            family = DEFAULT_FONT_MEDIUM
        ),
        alignment = Alignment.CENTER_LEFT
    )

    private val rotateLeftButton = Button(
        posX = 0, posY = 116,
        width = 170, height = 40,
        text = "Rotate Left",
        font = Font(size = 20, color = Color.WHITE, family = DEFAULT_FONT_BOLD),
        alignment = Alignment.CENTER_RIGHT,
        visual = Visual.EMPTY
    ).apply {
        componentStyle = "-fx-background-color: rgb(5,24,156);-fx-background-radius: $DEFAULT_BORDER_RADIUS"
        onMouseClicked = {
            activePlayerTile.rotation -= 90.0
        }
    }

    private val rotateLeftIcon = Label(
        posX = 10, posY = 124,
        width = 32, height = 27,
        visual = rotateLeftVisual
    )

    private val rotateRightButton = Button(
        posX = 185, posY = 116,
        width = 183, height = 40,
        text = "Rotate Right",
        font = Font(size = 20, color = Color.WHITE, family = DEFAULT_FONT_BOLD),
        alignment = Alignment.CENTER_RIGHT,
        visual = Visual.EMPTY
    ).apply {
        componentStyle = "-fx-background-color: rgb(5,24,156);-fx-background-radius: $DEFAULT_BORDER_RADIUS"
        onMouseClicked = {
            activePlayerTile.rotation += 90.0
        }
    }

    private val rotateRightIcon = Label(
        posX = 195, posY = 124,
        width = 32, height = 27,
        visual = rotateRightVisual
    )

    private val drawTileButton = Button(
        posX = 112, posY = 176,
        width = 152, height = 40,
        text = "Draw Tile",
        font = Font(size = 20, color = Color.WHITE, family = DEFAULT_FONT_BOLD),
        alignment = Alignment.CENTER_RIGHT,
        visual = Visual.EMPTY
    ).apply {
        componentStyle = "-fx-background-color: rgb(5,24,156);-fx-background-radius: $DEFAULT_BORDER_RADIUS"
    }

    private val drawTileIcon = Label(
        posX = 123, posY = 183,
        width = 30, height = 30,
        visual = drawTileVisual
    )

    init {
        addAll(
            activePlayerTile, activePlayerColorLabel, activePlayerNameLabel, activePlayerScoreLabel,
            rotateLeftButton, rotateLeftIcon, rotateRightButton, rotateRightIcon,
            drawTileButton, drawTileIcon
        )
    }
}