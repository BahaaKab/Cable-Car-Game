package view

import tools.aqua.bgw.core.MenuScene
import service.RootService
import tools.aqua.bgw.components.uicomponents.Button
import tools.aqua.bgw.core.Alignment
import tools.aqua.bgw.util.Font
import tools.aqua.bgw.visual.ColorVisual
import view.components.CableCarLogo
import java.awt.Color

@Suppress("UNUSED_PARAMETER","UNUSED","UndocumentedPublicFunction","UndocumentedPublicClass","EmptyFunctionBlock")



class LobbyScene(private val rootService: RootService) : MenuScene(1920, 1080), Refreshable {

    private val cableCarLogo = CableCarLogo(810,50).apply { scale = 1.1 }

    private val menuButton = Button(
        posX = 585, posY = 210,
        width = 110, height = 30,
        text = "Menu",
        font = Font(size = 21, color = Color.WHITE, family = DEFAULT_FONT_BOLD,
                fontWeight = Font.FontWeight.BOLD),
        alignment = Alignment.CENTER_RIGHT,
        visual = ColorVisual(249, 249, 250)
    ).apply { componentStyle = "-fx-background-color: rgba(233,233,236,1);-fx-background-radius: 100" }

    private val playerOrderButton = Button(
        posX = 720, posY = 210,
        width = 250, height = 30,
        text = "Shuffle Player Order",
        font = Font(size = 21, color = Color.WHITE, family = DEFAULT_FONT_BOLD,
            fontWeight = Font.FontWeight.BOLD),
        alignment = Alignment.CENTER_RIGHT,
        visual = ColorVisual(249, 249, 250)
    ).apply { componentStyle = "-fx-background-color: rgba(233,233,236,1);-fx-background-radius: 100" }

    private val tileRotationButton = Button(
        posX = 1000, posY = 210,
        width = 215, height = 30,
        text = "Enable Rotation",
        font = Font(size = 21, color = Color.WHITE, family = DEFAULT_FONT_BOLD,
            fontWeight = Font.FontWeight.BOLD),
        alignment = Alignment.CENTER_RIGHT,
        visual = ColorVisual(249, 249, 250)
    ).apply { componentStyle = "-fx-background-color: rgba(233,233,236,1);-fx-background-radius: 100" }

    init {
        opacity = 1.0
        background = ColorVisual(247, 247, 247)

        addComponents(cableCarLogo)
        addComponents(menuButton, playerOrderButton, tileRotationButton)
    }















    /**
     * @see view.Refreshable.refreshAfterStartGame
     */
    override fun refreshAfterStartGame() {
    }

    /**
     * @see view.Refreshable.refreshAfterHostGame
     */
    override fun refreshAfterHostGame() {
    }

    /**
     * @see view.Refreshable.refreshAfterJoinGame
     */
    override fun refreshAfterJoinGame() {
    }
}