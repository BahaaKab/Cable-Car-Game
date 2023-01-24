package view.components

import entity.PlayerType
import tools.aqua.bgw.components.layoutviews.Pane
import tools.aqua.bgw.components.uicomponents.*
import tools.aqua.bgw.core.Alignment
import tools.aqua.bgw.util.Font
import tools.aqua.bgw.visual.ColorVisual
import tools.aqua.bgw.visual.Visual
import view.CableCarApplication
import view.DEFAULT_BLUE
import view.DEFAULT_FONT_BOLD
import view.DEFAULT_FONT_MEDIUM
import java.awt.Color

/** A Pane which contains a "playerField" in the lobbyScene.
 *
 * Default width: 590, default height: 90
 *
 * @param posX Horizontal coordinate for this Pane. Default: 0.
 * @param posY Vertical coordinate for this Pane. Default: 0.
 * @param orderNumber An Integer that references the position of the Pane in comparison to others.
 * @param isNetwork A boolean that indicates which kind of Pane is needed
 * @param isHost A boolean that indicates if this Pane shows the Multiplayer-Host-Name
 * */
class InputPlayerPane(
    posX: Number = 0, posY: Number = 0, private var orderNumber : Int,
    private val isNetwork: Boolean = false, private val isHost: Boolean = false)
    : Pane<UIComponent>(posX, posY, width = 590, height = 90) {

    private val playerTypes = listOf(PlayerType.HUMAN, PlayerType.AI_EASY, PlayerType.AI_HARD)
    private var typeCounter = 0

    private val inputBackground = Label(
        posX = 20, posY = 15,
        width = 570, height = 75
    ).apply {
        componentStyle = "-fx-effect: dropshadow(one-pass-box, rgba(0,0,0,0.2), 10, 0, -1, 2);" +
                "-fx-background-radius: 10;-fx-background-color: rgba(255,255,255,1);"
    }

    private val circleLabel = Label(
        posX = 545, posY = 42,
        width = 25, height = 25
    ).apply {
        componentStyle = "-fx-background-radius: 100;-fx-background-color: rgba(255,255,255,1);" +
                "-fx-border-color: rgba(5, 24, 156, 1); -fx-border-radius: 100; -fx-border-width: 4;"
        onMouseClicked = {
                CableCarApplication.lobbyScene.playerOrderOptions(orderNumber)

        }
    }

    private val playerName = Label(
        posX = 50, posY = 38,
        width = 300, height = 30,
        alignment = Alignment.CENTER_LEFT,
        font = Font(
            size = 20, color = DEFAULT_BLUE, family = DEFAULT_FONT_MEDIUM,
            fontWeight = Font.FontWeight.BOLD
        ),
        text = "Waiting for Player..."
    ).apply { isVisible = false }

    private val nameField = TextField(
        posX = 40, posY = 30,
        width = 300, height = 30,
        prompt = "Type a name ...",
        font = Font(size = 24, family = DEFAULT_FONT_BOLD)
    ).apply {
        componentStyle = "-fx-background-color: rgba(0,0,0,0); -fx-border-color: rgb(5,24,156);" +
                " -fx-border-width: 0 0 3 0"
    }

    // Now following: The KickButton for the Host of Multiplayer-Lobbys

    private val kickButton = Button(
        posX = 7, posY = 8,
        width = 35, height = 20
    ).apply {
        componentStyle = "-fx-background-radius: 100;-fx-background-color: rgba(255,255,255,1);" +
                "-fx-border-color: rgba(212, 41, 38, 1); -fx-border-radius: 100; -fx-border-width: 4;" +
                "-fx-background-color: rgba(212, 41, 38, 1); " +
                "-fx-effect: dropshadow(one-pass-box, rgba(0,0,0,0.2), 10, 0, -1, 2);"
        scale = 0.8
    }

    private val crossLabel = Label(
        posX = 7, posY = 12,
        width = 35, height = 20,
        alignment = Alignment.CENTER,
        font = Font(size = 22, fontWeight = Font.FontWeight.BOLD, color = Color(255, 255, 255)),
        text = "X",
        visual = Visual.EMPTY
    )


    init {
        addAll(inputBackground, circleLabel, playerName)

        if (isNetwork) {
            multiPlayerConfiguration()
        } else {
            singlePlayerConfiguration()
        }
    }


    /** A Method for creating all & adding needed components for the Hot-Seat LobbyScene*/
    private fun singlePlayerConfiguration() {
        val playerType = Button(
            posX = 360, posY = 33,
            width = 145, height = 31,
            font = Font(size = 21, color = Color(233, 233, 236), family = DEFAULT_FONT_BOLD),
            text = "${playerTypes[typeCounter]}"
        ).apply {
            componentStyle = "-fx-background-color: rgba(5, 24, 156, 1);"
            onMouseClicked = {
                typeCounter = (typeCounter + 1) % 3
                this.text = "${playerTypes[typeCounter]}"
            }
        }

        addAll(playerType, nameField)
    }

    /** A Method for creating all needed components for the Multiplayer LobbyScene*/
    private fun multiPlayerConfiguration() {
        playerName.isVisible = true
        if (isHost) {
            val hostLabel = Label(
                posX = 420, posY = 36,
                width = 87, height = 34,
                font = Font(size = 23, color = Color(255, 255, 255), family = DEFAULT_FONT_BOLD),
                text = "HOST",
                visual = ColorVisual(233, 233, 236)
            )
            addAll(hostLabel)
        } else {
            addAll(kickButton, crossLabel)
        }
    }

    /** A Method to change the name of the displayed player name*/
    fun changePlayerName(name: String) {
        if (isNetwork) {
            playerName.text = name
        }
    }

    /** A Method to get the String that players are typing in the Textfield in Hot-Seat-Mode. */
    fun getTextFieldInput(): String {
        if(nameField.text.trim() == ""){
            nameField.text = "Player$orderNumber"
        }
        return nameField.text
    }

    /** Returns which playerType is currently selected in the comboBox. */
    fun getPlayerType() = playerTypes[typeCounter]

    /** Sets a new orderNumber. */
    fun setOrderNumber(number : Int) { orderNumber = number }

    /** A method that deactivates the kickButton. */
    fun deactivateKick(){
        kickButton.isVisible = false
        crossLabel.isVisible = false
    }
}