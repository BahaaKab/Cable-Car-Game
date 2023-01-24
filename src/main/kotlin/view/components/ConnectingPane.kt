package view.components

import tools.aqua.bgw.components.layoutviews.Pane
import tools.aqua.bgw.components.uicomponents.Label
import tools.aqua.bgw.components.uicomponents.UIComponent
import tools.aqua.bgw.util.Font
import view.DEFAULT_BLUE
import view.DEFAULT_FONT_BOLD


class ConnectingPane(posX: Int, posY: Int) :
    Pane<UIComponent>(posX, posY, 850, 240) {
    init {
        addAll(

            Label(width = 850, height = 400, posX = 535, posY = 390).apply {
                componentStyle = "-fx-effect: dropshadow(one-pass-box, rgba(0,0,0,0.2), 10, 0, -1, 2);" +
                        "-fx-background-radius: 10;-fx-background-color: rgba(255,255,255,1);"
            },

            Label(
                width = 300, height = 50,
                posX = 810, posY = 565,
                text = "Connecting ...",
                font = Font(size = 20, color = DEFAULT_BLUE, family = DEFAULT_FONT_BOLD)
            )
        )
    }
}