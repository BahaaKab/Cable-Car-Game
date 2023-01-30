package view.components

import service.AssetsLoader
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

    private val undoVisual = ImageVisual(AssetsLoader.undoImage)
    private val redoVisual = ImageVisual(AssetsLoader.redoImage)

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
            CableCarApplication.gameScene.updateImage()
        }
    }

    private val undoIcon = Label(
        posX = 12, posY = 11,
        width = 35, height = 20,
        visual = undoVisual
    ).apply {
        onMouseClicked = {
            CableCarApplication.gameScene.resetImage()
            rootService.playerActionService.undo()
            CableCarApplication.gameScene.updateImage()
        }
    }

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
    ).apply { onMouseClicked = { rootService.playerActionService.redo() } }

    private val selectAISpeedLabel = Label(
        posX = 280, posY = 0,
        width = 355, height = 40,
        text = "   AI Speed: ",
        font = Font(size = 20, color = Color.WHITE, family = DEFAULT_FONT_BOLD),
        alignment = Alignment.CENTER_LEFT
    ).apply {
        componentStyle = "-fx-background-color: rgb($DEFAULT_GREY_STRING);" +
                "-fx-background-radius: $DEFAULT_BORDER_RADIUS"
    }

    private val toggleGroup = ToggleGroup()

    private val speedRadioButtons = List(4) { i ->
        RadioButton(
            posX = 400 + i * 60, posY = 10,
            width = 5, height = 5,
            text = i.toString(),
            font = Font(size = 20, color = Color.WHITE, family = DEFAULT_FONT_BOLD),
            alignment = Alignment.CENTER_LEFT,
            toggleGroup = toggleGroup
        ).apply {
            onMouseClicked = { rootService.playerActionService.setAISpeed(i) }
        }
    }

    init {
        addAll(undoButton, undoIcon, redoButton, redoIcon, selectAISpeedLabel)
        addAll(speedRadioButtons)
    }

    /** Disables the Undo and Redo functionality */
    fun disableUndoRedo() {
        undoButton.apply {
            componentStyle = "-fx-background-color: rgb(127,127,127);-fx-background-radius: $DEFAULT_BORDER_RADIUS"
            isDisabled = true
        }
        undoIcon.apply { isDisabled = true }

        redoButton.apply {
            componentStyle = "-fx-background-color: rgb(127,127,127);-fx-background-radius: $DEFAULT_BORDER_RADIUS"
            isDisabled = true
        }
        redoIcon.apply { isDisabled = true }
    }

    /** Enables the Undo and Redo functionality */
    fun enableUndoRedo() {
        undoButton.apply {
            componentStyle = "-fx-background-color: rgb($DEFAULT_GREY_STRING);" +
                    "-fx-background-radius: $DEFAULT_BORDER_RADIUS"
            isDisabled = false
        }
        undoIcon.apply { isDisabled = false }

        redoButton.apply {
            componentStyle = "-fx-background-color: rgb($DEFAULT_GREY_STRING);" +
                    "-fx-background-radius: $DEFAULT_BORDER_RADIUS"
            isDisabled = false
        }
        redoIcon.apply { isDisabled = false }
    }

    /** Sets the AISpeed in the Data-Layer */
    internal fun setAISpeed(value : Int) {
        speedRadioButtons[value].isSelected = true
    }
}