package view.components

import entity.PlayerType
import tools.aqua.bgw.components.layoutviews.Pane
import tools.aqua.bgw.components.uicomponents.*
import tools.aqua.bgw.core.Alignment
import tools.aqua.bgw.util.Font
import tools.aqua.bgw.visual.ColorVisual
import tools.aqua.bgw.visual.Visual
import view.DEFAULT_BLUE
import view.DEFAULT_FONT_BOLD
import view.DEFAULT_FONT_MEDIUM
import java.awt.Color

/** A Pane which contains a "playerField" in the lobbyScene. Width: 755, Height: 75
 *
 * @param posX Horizontal coordinate for this Pane. Default: 0.
 * @param posY Vertical coordinate for this Pane. Default: 0.
 * @param playerNumber The Number of the Player (e.g. 1 -> "Player1")
 * @param color The default color of the player (e.g. ColorVisual(1,1,1) )
 * */
class InputPlayerPane(posX: Number = 0, posY: Number = 0, val playerNumber: Int, color: ColorVisual,
                      val isMultiplayer : Boolean = false, val isHost : Boolean = false) :
    Pane<UIComponent>(posX, posY, 0, 0) {

        private val playerTypes = listOf(PlayerType.HUMAN, PlayerType.AI_EASY, PlayerType.AI_HARD)
        private var typeCounter = 0

        private val colorLabel = Label(
            posX = 0, posY = 42,
            width = 75, height = 16,
            visual = color
        )

        private val playerLabel = Label(
            posX = 100, posY = 30,
            width = 90, height = 40,
            text = "Player $playerNumber:",
            alignment = Alignment.CENTER,
            font = Font(size = 21, color = DEFAULT_BLUE, family = DEFAULT_FONT_BOLD)
        )

        private val inputBackground = Label(
            posX = 200, posY = 15,
            width = 570, height = 75
        ).apply { componentStyle = "-fx-effect: dropshadow(one-pass-box, rgba(0,0,0,0.2), 10, 0, -1, 2);" +
                    "-fx-background-radius: 10;-fx-background-color: rgba(255,255,255,1);"}

        private val circleLabel = Label(
            posX = 725, posY = 42,
            width = 25, height = 25
        ).apply { componentStyle = "-fx-background-radius: 100;-fx-background-color: rgba(255,255,255,1);" +
                    "-fx-border-color: rgba(5, 24, 156, 1); -fx-border-radius: 100; -fx-border-width: 4;"}

        private val playerName = Label(
            posX = 230, posY = 38,
            width = 300, height = 30,
            alignment = Alignment.CENTER_LEFT,
            font = Font(size = 20, color = DEFAULT_BLUE, family = DEFAULT_FONT_MEDIUM,
                        fontWeight = Font.FontWeight.BOLD),
            text = "Waiting for Player..."
        ).apply { isVisible = false }

        init {
            addAll(inputBackground, colorLabel, playerLabel, circleLabel, playerName)

            if(isMultiplayer){
                multiPlayerConfiguration()
            }else{
                singlePlayerConfiguration()
            }
        }

        private fun singlePlayerConfiguration(){
            val playerType = Button(
                posX = 540, posY = 33,
                width = 145, height = 31,
                font = Font(size = 21, color = Color(233,233,236), family = DEFAULT_FONT_BOLD),
                text = "${playerTypes[typeCounter]}"
            ).apply { componentStyle = "-fx-background-color: rgba(5, 24, 156, 1);"
                onMouseClicked = { typeCounter = (typeCounter+1) % 3
                    this.text = "${playerTypes[typeCounter]}"
                }
            }

            val nameField = TextField(
                posX = 220, posY = 30,
                width = 300, height = 30,
                prompt = "Player $playerNumber",
                font = Font(size = 24, family = DEFAULT_FONT_BOLD)
            ).apply { componentStyle = "-fx-background-color: rgba(0,0,0,0); -fx-border-color: rgb(5,24,156);" +
                    " -fx-border-width: 0 0 3 0" }

            addAll(playerType, nameField)
        }

        private fun multiPlayerConfiguration(){
            playerName.isVisible = true
            if(isHost){
                val hostLabel = Label(
                    posX = 600, posY = 36,
                    width = 87, height = 34,
                    font = Font(size = 23, color = Color(255,255,255), family = DEFAULT_FONT_BOLD),
                    text = "HOST",
                    visual = ColorVisual(233,233,236)
                )
                addAll(hostLabel)
            }else{
                val kickButton = Button(
                    posX = 187, posY = 8,
                    width = 35, height = 20
                ).apply { componentStyle = "-fx-background-radius: 100;-fx-background-color: rgba(255,255,255,1);" +
                        "-fx-border-color: rgba(212, 41, 38, 1); -fx-border-radius: 100; -fx-border-width: 4;" +
                        "-fx-background-color: rgba(212, 41, 38, 1); " +
                        "-fx-effect: dropshadow(one-pass-box, rgba(0,0,0,0.2), 10, 0, -1, 2);"
                        scale = 0.8
                }
                val crossLabel = Label(
                    posX = 187, posY = 12,
                    width = 35, height = 20,
                    alignment = Alignment.CENTER,
                    font = Font(size = 22, fontWeight = Font.FontWeight.BOLD, color = Color(255,255,255)),
                    text = "X",
                    visual = Visual.EMPTY
                )
                addAll(kickButton, crossLabel)
            }
        }

        fun changePlayerName(name : String){
            if(isMultiplayer){
                playerName.text = name
            }
        }
    }