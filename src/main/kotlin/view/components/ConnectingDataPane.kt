package view.components

import tools.aqua.bgw.components.ComponentView
import tools.aqua.bgw.components.layoutviews.Pane
import tools.aqua.bgw.components.uicomponents.Label
import tools.aqua.bgw.util.Font
import view.DEFAULT_BLUE
import view.DEFAULT_FONT_BOLD
import view.DEFAULT_FONT_MEDIUM

class ConnectingDataPane(posX: Int, posY: Int) :
    Pane<ComponentView>(posX, posY, 850, 240) {
    init {
        addAll(

            Label(width = 850, height = 50, posX = 535, posY = 320).apply {
                componentStyle = "-fx-effect: dropshadow(one-pass-box, rgba(0,0,0,0.2), 10, 0, -1, 2);" +
                        "-fx-background-radius: 10;-fx-background-color: rgba(255,255,255,1);"
            },

            Label(
                width = 400, height = 50,
                posX = 400, posY = 320,
                text = "Session ID:",
                font = Font(size = 20, color = DEFAULT_BLUE, family = DEFAULT_FONT_BOLD)
            ),
            Label(
                posX = 650, posY = 325,
                width = 240, height = 40,
                font = Font(size = 20, color = DEFAULT_BLUE, family = DEFAULT_FONT_MEDIUM),
                text = "thisIsOurFavouriteGame"
            ),

            Label(
                width = 400, height = 50,
                posX = 800, posY = 320,
                text = "Secret:",
                font = Font(size = 20, color = DEFAULT_BLUE, family = DEFAULT_FONT_BOLD)
            ),
            Label(
                posX = 990, posY = 325,
                width = 270, height = 40,
                font = Font(size = 20, color = DEFAULT_BLUE, family = DEFAULT_FONT_MEDIUM),
                text = "AMIN4PRESIDENT"
            )


        )
    }
}