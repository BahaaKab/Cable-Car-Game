package view.components

import tools.aqua.bgw.components.ComponentView
import tools.aqua.bgw.components.layoutviews.Pane
import tools.aqua.bgw.components.uicomponents.Label
import tools.aqua.bgw.util.Font
import view.DEFAULT_BLUE
import view.DEFAULT_FONT_BOLD
import view.DEFAULT_SHADOW


class ConnectingPane(posX: Int, posY: Int) :
    Pane<ComponentView>(posX, posY, 850, 240) {
    init {
        addAll(

            Label(width = 850, height= 400, posX = 535, posY = 390).apply {
                componentStyle = "-fx-background-color: rgb(255,255,255);-fx-background-radius: 15px;"
                backgroundStyle = DEFAULT_SHADOW
            },
            Label(
                width = 300, height = 50,
                posX   = 810, posY = 565,
                text = "Connecting ...",
                font = Font(size = 20, color = DEFAULT_BLUE, family = DEFAULT_FONT_BOLD)
            ))
    }
    }