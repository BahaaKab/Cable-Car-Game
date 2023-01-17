package view

import service.RootService
import tools.aqua.bgw.components.uicomponents.Button
import tools.aqua.bgw.components.uicomponents.Label
import tools.aqua.bgw.core.Alignment
import tools.aqua.bgw.core.MenuScene
import tools.aqua.bgw.util.Font
import tools.aqua.bgw.visual.ColorVisual
import tools.aqua.bgw.visual.ImageVisual
import tools.aqua.bgw.visual.Visual
import view.components.CableCarLogo
import view.components.ConnectionPane
import view.components.HumanPlayerType
import java.awt.Color
import javax.imageio.ImageIO

class ConnectionScene(private val rootService: RootService) : MenuScene(1920, 1080), Refreshable {

    private val logoPane = CableCarLogo(posX = 841, posY = 104)

    private val somethingPane = ConnectionPane(0, 0)
    private val playerTypePane = HumanPlayerType(posX = 1200, posY = 365)

    private val menuVisual = ImageVisual(ImageIO.read(ConnectionScene::class.java.getResource("/arrow.png")))

    val menuButton = Button(
        posX = 535, posY = 256,
        width = 100, height = 40,
        text = "Menu",
        font = Font(size = 20, color = Color.WHITE, family = DEFAULT_FONT_BOLD),
        alignment = Alignment.BOTTOM_RIGHT,
        visual = Visual.EMPTY
    ).apply {
        componentStyle = "-fx-background-color: rgb($DEFAULT_GREY_STRING);-fx-background-radius: 20px"
    }
    val hostGameButton = Button(
        posX = 760, posY = 700,
        width = 175, height = 40,
        text = "Host Game",
        font = Font(size = 20, color = Color.WHITE, family = DEFAULT_FONT_BOLD),
        alignment = Alignment.CENTER,
        visual = Visual.EMPTY
    ).apply {
        componentStyle = "-fx-background-color: rgb(5,24,156);-fx-background-radius: 20px"
        onMouseClicked = { CableCarApplication.showMenuScene(CableCarApplication.connectingScene) }
    }

    val joinGameButton = Button(
        posX = 985, posY = 700,
        width = 175, height = 40,
        text = "Join Game",
        font = Font(size = 20, color = Color.WHITE, family = DEFAULT_FONT_BOLD),
        alignment = Alignment.CENTER,
        visual = Visual.EMPTY
    ).apply {
        componentStyle = "-fx-background-color: rgb(5,24,156);-fx-background-radius: 20px"
        onMouseClicked = { CableCarApplication.showMenuScene(CableCarApplication.connectingScene) }
    }

    init {
        opacity = 1.0
        background = ColorVisual(247, 247, 247)
        addComponents(
            logoPane,
            menuButton,
            Label(
                posX = 545, posY = 272,
                width = 22, height = 15,
                visual = menuVisual
            ),
            somethingPane,
            hostGameButton,
            joinGameButton,
            playerTypePane

        )
    }
}
