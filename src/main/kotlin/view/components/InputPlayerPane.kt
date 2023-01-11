package view.components

import entity.PlayerType
import tools.aqua.bgw.components.layoutviews.Pane
import tools.aqua.bgw.components.uicomponents.*
import tools.aqua.bgw.core.Alignment
import tools.aqua.bgw.util.Font
import tools.aqua.bgw.visual.ColorVisual
import view.DEFAULT_BLUE
import view.DEFAULT_FONT_BOLD
import java.awt.Color

/** A Pane which contains a "playerField" in the lobbyScene. Width: 755, Height: 75
 *
 * @param posX Horizontal coordinate for this Pane. Default: 0.
 * @param posY Vertical coordinate for this Pane. Default: 0.
 * @param playerNumber The Number of the Player (e.g. 1 -> "Player1")
 * @param color The default color of the player (e.g. ColorVisual(1,1,1) )
 * */
class InputPlayerPane(posX: Number = 0, posY: Number = 0, playerNumber: Int, color: ColorVisual) :
    Pane<UIComponent>(posX, posY, 0, 0) {

        private val colorLabel = Label(
            posX = 0, posY = 42,
            width = 75, height = 16,
            visual = color
        )

        private val playerLabel = Label(
            posX = 100, posY = 30,
            width = 90, height = 40,
            text = "Player$playerNumber:",
            alignment = Alignment.CENTER,
            font = Font(size = 21, color = DEFAULT_BLUE, family = DEFAULT_FONT_BOLD)
        )

        private val inputBackground = Label(
            posX = 200, posY = 15,
            width = 555, height = 75
        ).apply { componentStyle = "-fx-effect: dropshadow(one-pass-box, rgba(0,0,0,0.2), 10, 0, -1, 2);" +
                    "-fx-background-radius: 10;-fx-background-color: rgba(255,255,255,1);"}

        private val circleLabel = Label(
            posX = 710, posY = 42,
            width = 25, height = 25
        ).apply { componentStyle = "-fx-background-radius: 100;-fx-background-color: rgba(255,255,255,1);" +
                    "-fx-border-color: rgba(5, 24, 156, 1); -fx-border-radius: 100; -fx-border-width: 4;"}

        private val nameField = TextField(
            posX = 220, posY = 30,
            width = 300, height = 30,
            prompt = "Player $playerNumber",
            font = Font(size = 24, family = DEFAULT_FONT_BOLD)
        ).apply { componentStyle = "-fx-effect: dropshadow(one-pass-box, rgba(0,0,0,0.2), 10, 0, -1, 2);" }

        private val playerType = ComboBox(
            posX = 540, posY = 33,
            width = 130, height = 31,
            font = Font(size = 21, color = Color(233,233,236), family = DEFAULT_FONT_BOLD),
            prompt = "HUMAN",
            listOf(PlayerType.HUMAN,PlayerType.AI_EASY, PlayerType.AI_HARD)
        ).apply { componentStyle = "-fx-background-color: rgba(5, 24, 156, 1);" }

        init {
            addAll(inputBackground, colorLabel, playerLabel, nameField, playerType, circleLabel)
        }
    }