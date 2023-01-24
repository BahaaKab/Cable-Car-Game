package view.components

import entity.PlayerType
import tools.aqua.bgw.components.uicomponents.Button
import tools.aqua.bgw.core.Alignment
import tools.aqua.bgw.util.Font
import view.DEFAULT_BLUE_VISUAL
import view.DEFAULT_FONT_BOLD
import java.awt.Color

class PlayerTypeButton(posX: Int = 0, posY: Int = 0) : Button(
    posX = posX,
    posY = posY,
    width = 145,
    height = 31,
    font = Font(size = 21, color = Color(233, 233, 236), family = DEFAULT_FONT_BOLD),
    alignment = Alignment.CENTER,
    visual = DEFAULT_BLUE_VISUAL
) {
    private val playerTypes = listOf(PlayerType.HUMAN, PlayerType.AI_EASY, PlayerType.AI_HARD)
    private var selector = 0

    init {
        componentStyle = "-fx-background-color: rgba(5, 24, 156, 1);"
        text = playerTypes[selector].toString()
        onMouseClicked = {
            selector = (selector + 1) % playerTypes.size
            text = playerTypes[selector].toString()
        }
    }

    fun getPlayerType(): PlayerType = playerTypes[selector]
}