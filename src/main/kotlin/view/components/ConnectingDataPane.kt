package view.components

import tools.aqua.bgw.components.ComponentView
import tools.aqua.bgw.components.layoutviews.Pane
import tools.aqua.bgw.components.uicomponents.Label
import tools.aqua.bgw.util.Font
import view.DEFAULT_BLUE
import view.DEFAULT_FONT_BOLD
import view.DEFAULT_SHADOW

class ConnectingDataPane(posX: Int, posY: Int) :
    Pane<ComponentView>(posX, posY, 850, 240) {
    init {
        addAll(

            Label(width = 850, height = 50, posX = 535, posY = 320).apply {
                componentStyle = "-fx-background-color: rgb(255,255,255);-fx-background-radius: 15px;"
                backgroundStyle = DEFAULT_SHADOW
            },
            Label(
                width = 400, height = 50,
                posX = 400, posY = 320,
                text = "Session ID:",
                font = Font(size = 20, color = DEFAULT_BLUE, family = DEFAULT_FONT_BOLD)
            ),
            Label(
                width = 400, height = 50,
                posX = 800, posY = 320,
                text = "Secret:",
                font = Font(size = 20, color = DEFAULT_BLUE, family = DEFAULT_FONT_BOLD)
            )
        )
    }
}