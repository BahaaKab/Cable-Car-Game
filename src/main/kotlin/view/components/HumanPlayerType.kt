package view.components

import tools.aqua.bgw.components.layoutviews.Pane
import tools.aqua.bgw.components.uicomponents.Label
import tools.aqua.bgw.core.Alignment
import tools.aqua.bgw.util.Font
import view.DEFAULT_BLUE_VISUAL
import view.DEFAULT_FONT_BOLD
import java.awt.Color

class HumanPlayerType(posX: Number = 0, posY: Number = 0) :
    Pane<Label>(posX = posX, posY = posY, width = 0, height = 0) {

    init {
        addAll(
            Label(
                posX = 0, posY = 0,
                width = 119, height = 40,
                text = "HUMAN",
                font = Font(size = 25, color = Color.WHITE, family = DEFAULT_FONT_BOLD),
                alignment = Alignment.CENTER,
                visual = DEFAULT_BLUE_VISUAL
            )
        )
    }
}
