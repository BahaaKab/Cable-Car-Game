package view

import tools.aqua.bgw.core.BoardGameScene
import service.RootService
import tools.aqua.bgw.components.gamecomponentviews.CardView
import tools.aqua.bgw.components.layoutviews.GridPane
import tools.aqua.bgw.components.uicomponents.Label
import tools.aqua.bgw.core.Alignment
import tools.aqua.bgw.util.Font
import tools.aqua.bgw.visual.ColorVisual
import tools.aqua.bgw.visual.ImageVisual
import tools.aqua.bgw.visual.Visual
import java.awt.Color
import javax.imageio.ImageIO

@Suppress("UNUSED","UndocumentedPublicFunction","UndocumentedPublicClass","EmptyFunctionBlock")
class GameScene(private val rootService: RootService) : BoardGameScene(1920, 1080), Refreshable {

    private val tile3Visual = ImageVisual(ImageIO.read(GameScene::class.java.getResource("/tile3.png")))
    private val tile7Visual = ImageVisual(ImageIO.read(GameScene::class.java.getResource("/tile7.png")))

    private val boardVisual = ImageVisual(ImageIO.read(GameScene::class.java.getResource("/board.png")))

    private val logoPane = CableCarLogo(posX = 270, posY = 104)

    private val somethingPane = SomethingPane(100, 256)

    private val connectionStatusLabel = Label(
        posX = 50, posY= 950,
        width = 500, height = 80,
        text = "Connection successful. The game has started.",
        font = Font(size = 20, color = Color(13, 111, 47), family = DEFAULT_FONT, fontWeight = Font.FontWeight.BOLD),
        alignment = Alignment.CENTER,
        visual = Visual.EMPTY
    ).apply {
        componentStyle = "-fx-background-color: rgb(153, 192, 167);-fx-background-radius: 16px;" +
                "-fx-border-radius: 16px;-fx-border-width: 5px;-fx-border-color: rgb(19, 99, 43)"
    }

    private val activePlayerPane = ActivePlayerPane(90, 334)

    private val otherPlayersPane = OtherPlayersPane(100, 648)

    private val board = GridPane<CardView>(
        posX = 850, posY = 50,
        columns = 10, rows = 10,
        spacing = 1,
        layoutFromCenter = false,
        visual = boardVisual
    ).apply {
        setColumnWidths(93)
        setRowHeights(93)
        set(
            columnIndex = 4,
            rowIndex = 1,
            component = CardView(width = 93, height = 93, front = tile7Visual)
        )

        set(
            columnIndex = 8,
            rowIndex = 8,
            component = CardView(width = 93, height = 93,front = tile3Visual)
        )
    }

    init {
        background = ColorVisual(247,247,247)
        addComponents(
            logoPane,
            somethingPane,
            activePlayerPane,
            otherPlayersPane,
            connectionStatusLabel,
            board
        )
    }

    /**
     * @see view.Refreshable.refreshAfterEndGame
     */
    override fun refreshAfterEndGame() {
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

    /**
     * @see view.Refreshable.refreshAfterCalculatePoints
     */
    override fun refreshAfterCalculatePoints() {
    }

    /**
     * @see view.Refreshable.refreshAfterRotateTileLeft
     */
    override fun refreshAfterRotateTileLeft() {
    }

    /**
     * @see view.Refreshable.refreshAfterRotateTileRight
     */
    override fun refreshAfterRotateTileRight() {
    }

    /**
     * @see view.Refreshable.refreshAfterUndo
     */
    override fun refreshAfterUndo() {
    }

    /**
     * @see view.Refreshable.refreshAfterRedo
     */
    override fun refreshAfterRedo() {
    }

    /**
     * @see view.Refreshable.refreshAfterPlaceTile
     */
    override fun refreshAfterPlaceTile() {
    }

    /**
     * @see view.Refreshable.refreshAfterDrawTile
     */
    override fun refreshAfterDrawTile() {
    }

    /**
     * @see view.Refreshable.refreshAfterGetTurn
     */
    override fun refreshAfterGetTurn() {
    }

    /**
     * @see view.Refreshable.refreshAfterNextTurn
     */
    override fun refreshAfterNextTurn() {
    }
}