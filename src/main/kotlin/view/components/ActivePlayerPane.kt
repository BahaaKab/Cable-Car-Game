package view.components

import service.RootService
import tools.aqua.bgw.components.ComponentView
import tools.aqua.bgw.components.container.LinearLayout
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
class ActivePlayerPane(private val rootService: RootService, posX: Number = 0, posY: Number = 0) :
    Pane<ComponentView>(posX = posX, posY = posY, width = 630, height = 240) {

    private val rotateLeftVisual = ImageVisual(
        ImageIO.read(GameScene::class.java.getResource("/rotateLeft.png"))
    )
    private val rotateRightVisual = ImageVisual(
        ImageIO.read(GameScene::class.java.getResource("/rotateRight.png"))
    )
    private val drawTileVisual = ImageVisual(ImageIO.read(GameScene::class.java.getResource("/drawTile.png")))

    val activePlayerTiles = LinearLayout<CardView>(
        posX = 390, posY = 0,
        width = 260, height = 240,
        spacing = 20
    )

    private val activePlayerColorLabel = Label(
        posX = 10, posY = 26,
        width = 70, height = 10,
        visual = DEFAULT_YELLOW_COLOR
    )

    private val activePlayerNameLabel = Label(
        posX = 110, posY = 16,
        width = 100, height = 20,
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
            rootService.playerActionService.rotateTileLeft()
        }
    }

    private val rotateLeftIcon = Label(
        posX = 15, posY = 124,
        width = 25, height = 25,
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
            rootService.playerActionService.rotateTileRight()
        }
    }

    private val rotateRightIcon = Label(
        posX = 200, posY = 124,
        width = 25, height = 25,
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
        onMouseClicked = {
            rootService.playerActionService.drawTile()
        }
    }

    private val drawTileIcon = Label(
        posX = 123, posY = 180,
        width = 30, height = 30,
        visual = drawTileVisual
    )

    init {
        addAll(
            activePlayerTiles, activePlayerColorLabel, activePlayerNameLabel, activePlayerScoreLabel,
            rotateLeftButton, rotateLeftIcon, rotateRightButton, rotateRightIcon,
            drawTileButton, drawTileIcon
        )
    }

    fun refreshActivePlayer() = with(rootService.cableCar.currentState.activePlayer) {
        activePlayerTiles.clear()
        if (handTile == null) {
            activePlayerTiles.add(CardView(width = 100, height = 100, front = Visual.EMPTY))
        } else {
            activePlayerTiles.add(tileMapBig.forward(handTile!!.id).apply {
                opacity = 1.0
                rotation = handTile!!.rotation.toDouble()
            })
        }
        activePlayerNameLabel.text = name
        activePlayerScoreLabel.text = "Score: $score"
        activePlayerColorLabel.visual = when (color) {
            entity.Color.YELLOW -> DEFAULT_YELLOW_COLOR
            entity.Color.BLUE -> DEFAULT_BLUE_COLOR
            entity.Color.ORANGE -> DEFAULT_RED_COLOR
            entity.Color.GREEN -> DEFAULT_GREEN_COLOR
            entity.Color.PURPLE -> DEFAULT_PURPLE_COLOR
            entity.Color.BLACK -> DEFAULT_BLACK_COLOR
        }
    }

    fun disableTileRotationButtons() {
        rotateLeftButton.apply {
            componentStyle = "-fx-background-color: rgb(127,127,127);-fx-background-radius: $DEFAULT_BORDER_RADIUS"
            isDisabled = true
        }
        rotateRightButton.apply {
            componentStyle = "-fx-background-color: rgb(127,127,127);-fx-background-radius: $DEFAULT_BORDER_RADIUS"
            isDisabled = true
        }
    }

    fun disableDrawTileButton() = drawTileButton.apply {
        componentStyle = "-fx-background-color: rgb(127,127,127);-fx-background-radius: $DEFAULT_BORDER_RADIUS"
        isDisabled = true
    }


    fun enableDrawTileButton() = drawTileButton.apply {
        componentStyle = "-fx-background-color: rgb(5,24,156);-fx-background-radius: $DEFAULT_BORDER_RADIUS"
        isDisabled = false
    }

}