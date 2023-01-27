package view.components

import service.RootService
import tools.aqua.bgw.components.ComponentView
import tools.aqua.bgw.components.layoutviews.Pane
import tools.aqua.bgw.components.uicomponents.Button
import tools.aqua.bgw.components.uicomponents.Label
import tools.aqua.bgw.components.uicomponents.RadioButton
import tools.aqua.bgw.components.uicomponents.ToggleGroup
import tools.aqua.bgw.core.Alignment
import tools.aqua.bgw.util.Font
import tools.aqua.bgw.visual.ImageVisual
import tools.aqua.bgw.visual.Visual
import view.*
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
class OptionsPane(rootService: RootService, posX: Number = 0, posY: Number = 0) :
    Pane<ComponentView>(posX = posX, posY = posY, width = 402, height = 40) {

    private val undoVisual = ImageVisual(ImageIO.read(GameScene::class.java.getResource("/undo.png")))
    private val redoVisual = ImageVisual(ImageIO.read(GameScene::class.java.getResource("/redo.png")))

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
        onMouseClicked = {
            CableCarApplication.gameScene.resetImage()
            rootService.playerActionService.undo()
        }
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
        onMouseClicked = { rootService.playerActionService.redo() }
    }

    private val redoIcon = Label(
        posX = 145, posY = 11,
        width = 35, height = 20,
        visual = redoVisual
    )

    private val selectAISpeedLabel = Label(
        posX = 280, posY = 0,
        width = 250, height = 40,
        text = "    AI Speed: ",
        font = Font(size = 20, color = Color.WHITE, family = DEFAULT_FONT_BOLD),
        alignment = Alignment.CENTER_LEFT
    ).apply {
        componentStyle = "-fx-background-color: rgb($DEFAULT_GREY_STRING);" +
                "-fx-background-radius: $DEFAULT_BORDER_RADIUS"
    }

    private val toggleGroup = ToggleGroup()

    private val speed1 = RadioButton(
        posX = 400, posY = 10,
        width = 5, height = 5,
        text = "0",
        font = Font(size = 20, color = Color.WHITE, family = DEFAULT_FONT_BOLD),
        alignment = Alignment.CENTER_LEFT,
        toggleGroup = toggleGroup
    ).apply {
        onMouseClicked = { rootService.playerActionService.setAISpeed(0) }
    }

    private val speed2 = RadioButton(
        posX = 470, posY = 10,
        width = 5, height = 5,
        text = "4",
        font = Font(size = 20, color = Color.WHITE, family = DEFAULT_FONT_BOLD),
        alignment = Alignment.CENTER_LEFT,
        toggleGroup = toggleGroup,
        isSelected = true
    ).apply {
        onMouseClicked = { rootService.playerActionService.setAISpeed(4) }
    }

    init {
        addAll(undoButton, undoIcon, redoButton, redoIcon, selectAISpeedLabel, speed1, speed2)
    }

    fun disableUndoRedo() {
        undoButton.apply {
            componentStyle = "-fx-background-color: rgb(127,127,127);-fx-background-radius: $DEFAULT_BORDER_RADIUS"
            isDisabled = true
        }
        redoButton.apply {
            componentStyle = "-fx-background-color: rgb(127,127,127);-fx-background-radius: $DEFAULT_BORDER_RADIUS"
            isDisabled = true
        }
    }

    fun enableUndoRedo() {
        undoButton.apply {
            componentStyle = "-fx-background-color: rgb($DEFAULT_GREY_STRING);" +
                    "-fx-background-radius: $DEFAULT_BORDER_RADIUS"
            isDisabled = false
        }
        redoButton.apply {
            componentStyle = "-fx-background-color: rgb($DEFAULT_GREY_STRING);" +
                    "-fx-background-radius: $DEFAULT_BORDER_RADIUS"
            isDisabled = false
        }
    }
}