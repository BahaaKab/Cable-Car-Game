package view.components

import tools.aqua.bgw.components.ComponentView
import tools.aqua.bgw.components.layoutviews.Pane
import tools.aqua.bgw.components.uicomponents.Label
import tools.aqua.bgw.components.uicomponents.TextField
import tools.aqua.bgw.util.Font
import tools.aqua.bgw.visual.ColorVisual
import view.DEFAULT_BLUE
import view.DEFAULT_FONT_BOLD
import view.DEFAULT_YELLOW_COLOR
import java.awt.Color

class InputPlayerPane(posX: Number = 0, posY: Number = 0, playerNumber: Int, color: ColorVisual) :
    Pane<ComponentView>(posX, posY, 0, 0) {

        private val colorLabel = Label(
            posX = 0, posY = 35,
            width = 75, height = 30,
            visual = color
        )

        private val playerLabel = Label(
            posX = 100, posY = 30,
            width = 75, height = 40,
            text = "Player$playerNumber",
            Font(size = 38, color = DEFAULT_BLUE, family = DEFAULT_FONT_BOLD)
        )

        private val nameField = TextField(
            posX = 100, posY = 40,
            width = 300, height = 30,
            text = "Player$playerNumber",
            font = Font(size = 24, color = Color(233,233,236), family = DEFAULT_FONT_BOLD)
        )

        init {
            addAll(colorLabel, playerLabel, nameField)
        }
    }