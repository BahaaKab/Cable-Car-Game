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
import view.components.ConnectingDataPane
import view.components.ConnectingPane
import java.awt.Color
import javax.imageio.ImageIO

/** The class which administrate all Components to show the Connection to Network-Lobby
 *
 * @param rootService A connection to the administration of all Game-Data */
class ConnectingScene(private val rootService: RootService) : MenuScene(1920, 1080), Refreshable {
    private val logoPane = CableCarLogo(posX = 841, posY = 104)
    private val cancelVisual = ImageVisual(ImageIO.read(ConnectionScene::class.java.getResource("/arrow.png")))
    private val connectingPane = ConnectingPane(0, 0)
    private val connectingDataPane = ConnectingDataPane(0, 0)

    private val cancelButton = Button(
        posX = 535, posY = 256,
        width = 120, height = 40,
        text = "Cancel",
        font = Font(size = 20, color = Color.WHITE, family = DEFAULT_FONT_BOLD),
        alignment = Alignment.BOTTOM_RIGHT,
        visual = Visual.EMPTY
    ).apply { componentStyle = "-fx-background-color: rgb($DEFAULT_GREY_STRING);-fx-background-radius: 20px"
        onMouseClicked = { CableCarApplication.showMenuScene(CableCarApplication.chooseModeScene) }
    }

    init {
        opacity = 1.0
        background = ColorVisual(247, 247, 247)
        addComponents(
            connectingPane,
            connectingDataPane,
            logoPane,
            cancelButton,
            Label(
                posX = 545, posY = 272,
                width = 22, height = 15,
                visual = cancelVisual
            ).apply { onMouseClicked = { CableCarApplication.showMenuScene(CableCarApplication.chooseModeScene) } }
        )
    }

    /** A Method to set all sessionID */
    fun setData(sessionID : String) {
        connectingDataPane.setSessionID(sessionID)
    }
}

