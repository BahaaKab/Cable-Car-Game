package view

import tools.aqua.bgw.core.MenuScene
import service.RootService
import tools.aqua.bgw.components.uicomponents.Button
import tools.aqua.bgw.components.uicomponents.Label
import tools.aqua.bgw.core.Alignment
import tools.aqua.bgw.util.Font
import tools.aqua.bgw.visual.ColorVisual
import java.awt.Color

@Suppress("UNUSED_PARAMETER","UNUSED","UndocumentedPublicFunction","UndocumentedPublicClass","EmptyFunctionBlock")


class ChooseModeScene(private val rootService: RootService) : MenuScene(1920, 1080), Refreshable {

    // Farbe London Underground/tfl: 36, 53, 136,1
    // Farbe unsere GUI-Vorlage: 5, 24, 156, 1

    private val cableCarLabel : Label = Label(
        posX = 640, posY = 40,
        width = 640, height = 170,
        text = "CABLECAR",
        font = Font(size = 120, color = Color.WHITE,
            fontWeight = Font.FontWeight.BOLD, family = "Johnston ITC Std Light" ),
        alignment = Alignment.CENTER,
        visual = ColorVisual(5,24,156)
    )

    private val undergroundLabel : Label = Label(
        posX = 660, posY= 208,
        width = 600, height = 80,
        text = "UNDERGROUND",
        font = Font(size = 70, color = Color(5,24,156),
            fontWeight = Font.FontWeight.BOLD, family = "Johnston ITC Std Light"),
        alignment = Alignment.CENTER
    )

    private val chooseModeLabel : Label = Label(
        posX = 560, posY = 380,
        width = 800, height = 140,
        text = "Which type of Game would you like to play?",
        font = Font(size = 20, color = Color(5,24,156)
            , fontWeight = Font.FontWeight.SEMI_BOLD, family = "Johnston ITC Std Medium"),
        alignment = Alignment.CENTER
    ).apply { componentStyle = ("-fx-background-color: rgba(255,255,255,1);-fx-background-radius: 30;"+
            "-fx-effect: dropshadow(one-pass-box, rgba(0,0,0,0.2), 10, 0, -1, 2);") }

    private val localButton : Button = Button(
        posX = 860, posY = 580,
        width = 200, height = 50,
        text = "Local Game",
        font = Font(size = 23, color = Color.WHITE, family = "Johnston ITC Std Light"),
        alignment = Alignment.CENTER,
        visual = ColorVisual(249, 249, 250)
    ).apply { componentStyle = "-fx-background-color: rgba(5,24,156,1);-fx-background-radius: 100" }

    private val networkButton : Button = Button(
        posX = 860, posY = 680,
        width = 200, height = 50,
        text = "Network Game",
        font = Font(size = 23, color = Color.WHITE, family = "Johnston ITC Std Light"),
        alignment = Alignment.CENTER,
        visual = ColorVisual(249, 249, 250)
    ).apply { componentStyle = "-fx-background-color: rgba(5,24,156,1);-fx-background-radius: 100" }

// Beispiel für Hintergrundfarbe: "-fx-background-color: rgba(150,40,173,1);
    init {
        opacity = 1.0
        background = ColorVisual(247, 247, 247)
        addComponents(cableCarLabel, undergroundLabel, chooseModeLabel)
        addComponents(localButton, networkButton)
    }
}