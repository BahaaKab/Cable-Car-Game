package view

import tools.aqua.bgw.core.MenuScene
import service.RootService
import tools.aqua.bgw.components.uicomponents.Button
import tools.aqua.bgw.components.uicomponents.Label
import tools.aqua.bgw.core.Alignment
import tools.aqua.bgw.util.Font
import tools.aqua.bgw.visual.ColorVisual
import tools.aqua.bgw.visual.ImageVisual
import view.components.CableCarLogo
import view.components.InputPlayerPane
import view.components.NumberOfPlayersPane
import java.awt.Color
import javax.imageio.ImageIO


/** LobbyScene shows the settings- & Player-Lobby in HotSeat-Mode.
 *
 * @param rootService A reference to the administration of the Game-State*/
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

    private val backArrow = Label(
        posX = 594, posY = 215,
        width = 30, height = 30,
        visual = ImageVisual(ImageIO.read(LobbyScene::class.java.getResource("/arrow_back.jpg")))
    )

    private val playerOrderButton = Button(
        posX = 720, posY = 210,
        width = 264, height = 30,
        text = "Shuffle Player Order",
        font = Font(size = 21, color = Color.WHITE, family = DEFAULT_FONT_BOLD,
            fontWeight = Font.FontWeight.BOLD),
        alignment = Alignment.CENTER_RIGHT,
        visual = ColorVisual(249, 249, 250)
    ).apply { componentStyle = "-fx-background-color: rgba(233,233,236,1);-fx-background-radius: 100" }

    private val cubePicture = Label(
        posX = 731, posY = 216,
        width = 28, height = 28,
        visual = ImageVisual(ImageIO.read(LobbyScene::class.java.getResource("/cube.jpg")))
    )

    private val tileRotationButton = Button(
        posX = 1014, posY = 210,
        width = 215, height = 30,
        text = "Enable Rotation",
        font = Font(size = 21, color = Color.WHITE, family = DEFAULT_FONT_BOLD,
            fontWeight = Font.FontWeight.BOLD),
        alignment = Alignment.CENTER_RIGHT,
        visual = ColorVisual(249, 249, 250)
    ).apply { componentStyle = "-fx-background-color: rgba(233,233,236,1);-fx-background-radius: 100" }

    private val refreshArrow = Label(
        posX = 1024, posY = 216,
        width = 28, height = 28,
        visual = ImageVisual(ImageIO.read(LobbyScene::class.java.getResource("/arrow_refresh.jpg")))
    )

    private val player2Display = NumberOfPlayersPane(555, 290, 2)
    private val player3Display = NumberOfPlayersPane(680, 290, 3)
    private val player4Display = NumberOfPlayersPane(805, 290, 4)
    private val player5Display = NumberOfPlayersPane(930, 290, 5)
    private val player6Display = NumberOfPlayersPane(1055, 290, 6)

    // private val playerXDisplay = NumberOfPlayersPane( 555 + 125 * (X-1), 290, X)

    private val player1 = InputPlayerPane(575, 375 , 1, DEFAULT_YELLOW_COLOR)
    private val player2 = InputPlayerPane(575, 465, 2, DEFAULT_BLUE_COLOR)
    private val player3 = InputPlayerPane(575, 555, 3, DEFAULT_RED_COLOR)
    private val player4 = InputPlayerPane(575, 645, 4, DEFAULT_GREEN_COLOR)
    private val player5 = InputPlayerPane(575, 735, 5, DEFAULT_PURPLE_COLOR)
    private val player6 = InputPlayerPane(575, 825, 6, DEFAULT_BLACK_COLOR)

    // private val playerX = InputPlayerPane( 575, 375 + 90 * (X-1), X, ColorX)

    private val startButton = Button(
        posX = 860, 950,
        width = 200, height = 50,
        text = "Start Game",
        font = Font(size = 23, color = Color.WHITE, family = DEFAULT_FONT_BOLD),
        alignment = Alignment.CENTER,
        visual = ColorVisual(249, 249, 250)
    ).apply { componentStyle = "-fx-background-color: rgba(5,24,156,1);-fx-background-radius: 100" }


    init {
        opacity = 1.0
        background = ColorVisual(247, 247, 247)

        addComponents(cableCarLogo, player2Display, player3Display, player4Display, player5Display, player6Display)
        addComponents(menuButton, playerOrderButton, tileRotationButton, startButton)
        addComponents(backArrow, cubePicture, refreshArrow)
        addComponents(player1,player2,player3,player4,player5,player6)
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