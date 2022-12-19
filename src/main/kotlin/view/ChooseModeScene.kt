package view

import tools.aqua.bgw.core.MenuScene
import service.RootService
import tools.aqua.bgw.components.uicomponents.Label
import tools.aqua.bgw.core.Alignment
import tools.aqua.bgw.util.Font
import tools.aqua.bgw.visual.ColorVisual

@Suppress("UNUSED_PARAMETER","UNUSED","UndocumentedPublicFunction","UndocumentedPublicClass","EmptyFunctionBlock")


class ChooseModeScene(private val rootService: RootService) : MenuScene(1920, 1080), Refreshable {

    private val cableCarLabel : Label = Label(
        posX = 640, posY = 40,
        width = 640, height = 170,
        text = "CABLECAR",
        font = Font(size = 100, color = java.awt.Color.WHITE),
        alignment = Alignment.CENTER,
        visual = ColorVisual(14, 40, 130)
    )

    private val undergroundLabel : Label = Label(
        posX = 660, posY= 205,
        width = 600, height = 80,
        text = "UNDERGROUND",
        font = Font(size = 60, color = java.awt.Color(14, 40, 130)),
        alignment = Alignment.CENTER
    )

    init {
        opacity = 1.0
        background = ColorVisual(249, 249, 250)
        addComponents(cableCarLabel, undergroundLabel)
    }
}