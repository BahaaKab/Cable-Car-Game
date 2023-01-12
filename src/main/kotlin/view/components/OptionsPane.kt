package view.components

import tools.aqua.bgw.components.ComponentView
import tools.aqua.bgw.components.layoutviews.Pane
import tools.aqua.bgw.components.uicomponents.Button
import tools.aqua.bgw.components.uicomponents.Label
import tools.aqua.bgw.core.Alignment
import tools.aqua.bgw.util.Font
import tools.aqua.bgw.visual.ImageVisual
import tools.aqua.bgw.visual.Visual
import view.DEFAULT_BORDER_RADIUS
import view.DEFAULT_FONT_BOLD
import view.DEFAULT_GREY_STRING
import view.GameScene
import java.awt.Color
import javax.imageio.ImageIO

/**
 * The Pane which contains buttons like undo, redo, and settings.
 *
 * Default width: 402, default height: 40.
 *
 * @param posX Horizontal coordinate for this Pane. Default: 0.
 * @param posY Vertical coordinate for this Pane. Default: 0.
 */
class OptionsPane(posX: Number = 0, posY: Number = 0) :
    Pane<ComponentView>(posX = posX, posY = posY, width = 402, height = 40) {

    private val undoVisual = ImageVisual(ImageIO.read(GameScene::class.java.getResource("/undo.png")))
    private val redoVisual = ImageVisual(ImageIO.read(GameScene::class.java.getResource("/redo.png")))
    private val settingsVisual = ImageVisual(ImageIO.read(GameScene::class.java.getResource("/settings.png")))

    private val undoButton = Button(
        posX = 0, posY = 0,
        width = 126, height = 40,
        text = "Undo",
        font = Font(size = 20, color = Color.WHITE, family = DEFAULT_FONT_BOLD),
        alignment = Alignment.BOTTOM_RIGHT,
        visual = Visual.EMPTY
    ).apply {
        componentStyle = "-fx-background-color: rgb($DEFAULT_GREY_STRING);" +
                "-fx-background-radius: $DEFAULT_BORDER_RADIUS"
    }

    private val undoIcon = Label(
        posX = 12, posY = 11,
        width = 35, height = 20,
        visual = undoVisual
    )

    private val redoButton = Button(
        posX = 135, posY = 0,
        width = 126, height = 40,
        text = "Redo",
        font = Font(size = 20, color = Color.WHITE, family = DEFAULT_FONT_BOLD),
        alignment = Alignment.BOTTOM_RIGHT,
        visual = Visual.EMPTY
    ).apply {
        componentStyle = "-fx-background-color: rgb($DEFAULT_GREY_STRING);" +
                "-fx-background-radius: $DEFAULT_BORDER_RADIUS"
    }

    private val redoIcon = Label(
        posX = 145, posY = 11,
        width = 35, height = 20,
        visual = redoVisual
    )

    private val settingsButton = Button(
        posX = 270, posY = 0,
        width = 132, height = 40,
        text = "Settings",
        font = Font(size = 20, color = Color.WHITE, family = DEFAULT_FONT_BOLD),
        alignment = Alignment.BOTTOM_RIGHT,
        visual = Visual.EMPTY
    ).apply {
        componentStyle = "-fx-background-color: rgb($DEFAULT_GREY_STRING);" +
                "-fx-background-radius: $DEFAULT_BORDER_RADIUS;"
    }

    private val settingsIcon = Label(
        posX = 278, posY = 9,
        width = 27, height = 27,
        visual = settingsVisual
    )

    init {
        addAll(undoButton, undoIcon, redoButton, redoIcon, settingsButton, settingsIcon)
    }
}