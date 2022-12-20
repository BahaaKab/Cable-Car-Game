package view

import tools.aqua.bgw.core.MenuScene
import service.RootService
import tools.aqua.bgw.components.uicomponents.Button
import tools.aqua.bgw.components.uicomponents.Label
import tools.aqua.bgw.core.Alignment
import tools.aqua.bgw.util.Font
import tools.aqua.bgw.visual.ColorVisual
import java.awt.Color
import javafx.*

@Suppress("UNUSED_PARAMETER","UNUSED","UndocumentedPublicFunction","UndocumentedPublicClass","EmptyFunctionBlock")


class ChooseModeScene(private val rootService: RootService) : MenuScene(1920, 1080), Refreshable {

    private val cableCarLabel : Label = Label(
        posX = 640, posY = 40,
        width = 640, height = 170,
        text = "CABLECAR",
        font = Font(size = 100, color = Color.WHITE),
        alignment = Alignment.CENTER,
        visual = ColorVisual(14, 40, 130)
    )

    private val undergroundLabel : Label = Label(
        posX = 660, posY= 205,
        width = 600, height = 80,
        text = "UNDERGROUND",
        font = Font(size = 60, color = Color(14, 40, 130)),
        alignment = Alignment.CENTER
    )

    private val chooseModeLabel : Label = Label(
        posX = 560, posY = 300,
        width = 800, height = 250,
        text = "Which type of Game would you like to play?",
        font = Font(size = 20, color = Color(14, 40, 130)),
        alignment = Alignment.CENTER
    )//.apply { componentStyle = "-fx-effect: dropshadow( gaussian , rgba(0,0,0,0.2) , 10,0,2,4 );" }

    private val localButton : Button = Button(
        posX = 860, posY = 580,
        width = 200, height = 50,
        text = "Local Game",
        font = Font(size = 23, color = Color.WHITE),
        alignment = Alignment.CENTER,
        visual = ColorVisual(249, 249, 250)
    ).apply { componentStyle = "-fx-background-color: rgba(14,40,130,1);-fx-background-radius: 100" }

    private val networkButton : Button = Button(
        posX = 860, posY = 680,
        width = 200, height = 50,
        text = "Network Game",
        font = Font(size = 23, color = Color.WHITE),
        alignment = Alignment.CENTER,
        visual = ColorVisual(249, 249, 250)
    ).apply { componentStyle = "-fx-background-color: rgba(14,40,130,1);-fx-background-radius: 100" }

// Beispiel für Hintergrundfarbe: "-fx-background-color: rgba(150,40,173,1);
    init {
        opacity = 1.0
        background = ColorVisual(249, 249, 250)
        addComponents(cableCarLabel, undergroundLabel, chooseModeLabel)
        addComponents(localButton, networkButton)
    }
}