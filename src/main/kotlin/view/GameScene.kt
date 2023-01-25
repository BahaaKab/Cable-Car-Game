package view

import edu.udo.cs.sopra.ntf.TileInfo
import entity.GameMode
import entity.GameTile
import entity.PlayerType
import entity.StationTile
import service.RootService
import tools.aqua.bgw.animation.DelayAnimation
import tools.aqua.bgw.animation.FadeAnimation
import tools.aqua.bgw.components.gamecomponentviews.CardView
import tools.aqua.bgw.components.layoutviews.GridPane
import tools.aqua.bgw.components.uicomponents.Label
import tools.aqua.bgw.core.BoardGameApplication
import tools.aqua.bgw.core.BoardGameScene
import tools.aqua.bgw.util.BidirectionalMap
import tools.aqua.bgw.util.Font
import tools.aqua.bgw.visual.ColorVisual
import tools.aqua.bgw.visual.ImageVisual
import tools.aqua.bgw.visual.Visual
import view.components.ActivePlayerPane
import view.components.CableCarLogo
import view.components.OptionsPane
import view.components.OtherPlayersPane
import java.awt.Color
import javax.imageio.ImageIO


val tileMapBig = BidirectionalMap<Int, CardView>().apply {
    for (i in 0..59) {
        add(i, CardView(width = 240, height = 240, front = ImageVisual(TILEIMAGELOADER.frontImageFor(i))))
    }
}

val tileMapSmall = BidirectionalMap<Int, CardView>().apply {
    for (i in 0..59) {
        add(i, CardView(width = 100, height = 100, front = ImageVisual(TILEIMAGELOADER.frontImageFor(i))))
    }
}

/**
 * This class manages the game scene of the application. Here a player can make a move.
 *
 * @param rootService The administration class for the entity and service layer.
 */
class GameScene(private val rootService: RootService) : BoardGameScene(1920, 1080), Refreshable {

    private val boardVisual = ImageVisual(ImageIO.read(GameScene::class.java.getResource("/board.png")))

    private val stationTileMap = BidirectionalMap<StationTile, CardView>()

    private val logoPane = CableCarLogo(posX = 270, posY = 104)

    private val optionsPane = OptionsPane(rootService = rootService, posX = 100, posY = 256)

    private val connectionStatusLabel = Label(
        posX = 50, posY = 950,
        width = 500, height = 80,
        text = "Connection successful. The game has started.",
        font = Font(size = 20, color = Color(13, 111, 47), family = DEFAULT_FONT_BOLD)
    ).apply {
        componentStyle = "-fx-background-color: rgb(153, 192, 167);-fx-background-radius: 16px;" +
                "-fx-border-radius: 16px;-fx-border-width: 5px;-fx-border-color: rgb(19, 99, 43)"
    }

    private val activePlayerPane = ActivePlayerPane(rootService = rootService, posX = 90, posY = 334)

    private val otherPlayersPane = OtherPlayersPane(rootService = rootService, posX = 100, posY = 648)

    private var placeablePositions: Set<Pair<Int, Int>> = setOf()

    private val emptyTilesCardViews = List(8) { column ->
        List(8) { row ->
            CardView(
                width = 100,
                height = 100,
                front = Visual.EMPTY,
                back = ColorVisual(247, 247, 247).apply {
                    style = """-fx-border-width: 2px; -fx-border-color: rgba(0,0,0,0.1);
                         -fx-effect:innershadow(one-pass-box, rgba(0,0,0,0.1), 30, 0.2, 0, 0);"""
                }
            ).apply {
                onMouseClicked = { rootService.playerActionService.placeTile(posX = column + 1, posY = row + 1) }
                showFront()
            }
        }
    }

    private val board = GridPane<CardView>(
        posX = 850, posY = 50,
        columns = 10, rows = 10,
        spacing = 1,
        layoutFromCenter = false,
        visual = boardVisual
    ).apply {
        setColumnWidths(100)
        setRowHeights(100)
        for (i in 1..8) {
            for (j in 1..8) {

                // We don't want to place empty tiles where the power stations are
                if ((i == 4 || i == 5) && (j == 4 || j == 5)) continue

                set(columnIndex = i, rowIndex = j, component = emptyTilesCardViews[i - 1][j - 1])
            }
        }
    }


    init {
        background = ColorVisual(247, 247, 247)
        addComponents(
            logoPane,
            optionsPane,
            activePlayerPane,
            otherPlayersPane,
            connectionStatusLabel,
            board
        )
    }

    private fun initializeStationTileMap() = with(rootService.cableCar.currentState) {
        players.forEach {
            it.stationTiles.forEach { station ->
                stationTileMap.add(
                    station,
                    CardView(width = 100, height = 100, front = ImageVisual((TILEIMAGELOADER.stationTileFor(it.color))))
                )
            }
        }
    }

    private fun initializeStationTiles() {
        checkNotNull(rootService.cableCar)

        // rotate(90) means rotate right by 90 degrees
        with(rootService.cableCar.currentState.board) {
            for (i in 1..8) {
                // on the left side
                board.set(columnIndex = 0, rowIndex = i,
                    component = stationTileMap.forward(this[0][i] as StationTile).apply { rotate(-90) })

                // on the right side
                try {
                    board.set(columnIndex = 9, rowIndex = i,
                        component = stationTileMap.forward(this[9][i] as StationTile).apply { rotate(90) })
                } catch (_: Exception) {
                }

                // on the top side
                board.set(
                    columnIndex = i, rowIndex = 0, component = stationTileMap.forward(this[i][0] as StationTile)
                )

                // on the bottom side
                try {
                    board.set(columnIndex = i, rowIndex = 9,
                        component = stationTileMap.forward(this[i][9] as StationTile).apply { rotate(180) })
                } catch (_: Exception) {
                }
            }
        }
    }

    private fun refreshBoard(oldState: entity.State) {
        val tilesToChange = mutableListOf<TileInfo>()
        val currentStateCopy = rootService.cableCar.currentState.deepCopy()
        var posX: Int
        var posY: Int

        // We are doing an undo
        if (oldState.placedTiles.size > currentStateCopy.placedTiles.size) {
            repeat(oldState.players.size) {
                tilesToChange.add(oldState.placedTiles.removeLast())
            }
            for (tileInfo in tilesToChange) {
                posX = tileInfo.x
                posY = tileInfo.y
                board.set(columnIndex = posX, rowIndex = posY, component = emptyTilesCardViews[posX - 1][posY - 1])
            }
        }

        // We are doing a redo
        else {
            repeat(oldState.players.size) {
                tilesToChange.add(currentStateCopy.placedTiles.removeLast())
            }
            for (tileInfo in tilesToChange) {
                posX = tileInfo.x
                posY = tileInfo.y
                board.set(columnIndex = posX, rowIndex = posY, component = tileMapSmall.forward(tileInfo.id))
            }
        }
    }

    /**
     * @see view.Refreshable.refreshAfterStartGame
     */
    override fun refreshAfterStartGame() {
        initializeStationTileMap()
        initializeStationTiles()
        if (!rootService.cableCar.allowTileRotation) activePlayerPane.disableTileRotationButtons()

        // Only show the connectionStatusLabel when Network Mode was chosen
        // Also show it for only 5 seconds
        if (rootService.cableCar.gameMode == GameMode.NETWORK) {
            optionsPane.disableUndoRedo()
            BoardGameApplication.runOnGUIThread {
                playAnimation(
                    FadeAnimation(
                        componentView = connectionStatusLabel,
                        fromOpacity = 1.0, toOpacity = 0.0,
                        duration = 5 * 1000
                    ).apply { onFinished = { componentView.opacity = 0.0 } }
                )
            }
        } else connectionStatusLabel.isVisible = false

        otherPlayersPane.refreshAfterStartGame()
        refreshAfterNextTurn()
    }

    /**
     * @see view.Refreshable.refreshAfterRotateTileLeft
     */
    override fun refreshAfterRotateTileLeft() {
        activePlayerPane.activePlayerTiles.last().rotate(-90)
    }

    /**
     * @see view.Refreshable.refreshAfterRotateTileRight
     */
    override fun refreshAfterRotateTileRight() {
        activePlayerPane.activePlayerTiles.last().rotate(90)
    }

    /**
     * @see view.Refreshable.refreshAfterUndo
     */
    override fun refreshAfterUndo(oldState: entity.State) {
        refreshBoard(oldState)
        activePlayerPane.refreshActivePlayer()
        otherPlayersPane.refreshOtherPlayers()
    }

    /**
     * @see view.Refreshable.refreshAfterRedo
     */
    override fun refreshAfterRedo(oldState: entity.State) = refreshAfterUndo(oldState)

    /**
     * @see view.Refreshable.refreshAfterPlaceTile
     */
    override fun refreshAfterPlaceTile(posX: Int, posY: Int) {
        activePlayerPane.activePlayerTiles.remove(activePlayerPane.activePlayerTiles.last())
        board.set(columnIndex = posX, rowIndex = posY, component = tileMapSmall.forward(
            (rootService.cableCar.currentState.board[posX][posY]!! as GameTile).id
        ).apply {
            rotation = (rootService.cableCar.currentState.board[posX][posY]!! as GameTile).rotation.toDouble()
        })
    }


    /**
     * @see view.Refreshable.refreshAfterDrawTile
     */
    override fun refreshAfterDrawTile() {
        if (rootService.cableCar.currentState.activePlayer.currentTile == null) {
            activePlayerPane.activePlayerTiles.add(
                tileMapBig.forward(rootService.cableCar.currentState.activePlayer.handTile!!.id)
            )
        } else {
            activePlayerPane.activePlayerTiles.first().apply { opacity = 0.5 }

            activePlayerPane.activePlayerTiles.add(
                tileMapBig.forward(
                    rootService.cableCar.currentState.activePlayer.currentTile!!.id
                )
            )
            activePlayerPane.disableDrawTileButton()
        }
    }

    /**
     * @see view.Refreshable.refreshAfterNextTurn
     */
    override fun refreshAfterNextTurn() {
        showPlaceablePositions()
        if (rootService.cableCar.currentState.drawPile.isEmpty()) {
            activePlayerPane.disableDrawTileButton()
        } else {
            activePlayerPane.enableDrawTileButton()
        }

        if(rootService.cableCar.gameMode == GameMode.HOTSEAT) { optionsPane.enableUndoRedo() }
        if(rootService.cableCar.currentState.activePlayer.isNetworkPlayer) {
            activePlayerPane.disableDrawTileButton()
            activePlayerPane.disableTileRotationButtons()
            lock()
        } else { unlock() }

        activePlayerPane.refreshActivePlayer()
        otherPlayersPane.refreshOtherPlayers()

        startAIHandler()
    }

    private fun showPlaceablePositions() {
        val currentTile = with(rootService.cableCar.currentState.activePlayer) {
            checkNotNull(currentTile ?: handTile)
        }
        val rotationAllowed = rootService.cableCar.allowTileRotation
        val newPlaceablePositions = rootService.playerActionService.getPlaceablePositions(
            currentTile,
            rotationAllowed
        )
        newPlaceablePositions.forEach { (x, y) ->
            board[x, y]?.showBack()
        }
        (placeablePositions - newPlaceablePositions).forEach { (x, y) ->
            board[x, y]?.showFront()
        }
        placeablePositions = newPlaceablePositions

    }

    private fun startAIHandler() {
        if (rootService.cableCar.currentState.activePlayer.playerType in listOf(
                PlayerType.AI_EASY,
                PlayerType.AI_HARD
            )
        ) {
            activePlayerPane.disableDrawTileButton()
            optionsPane.disableUndoRedo()

            playAnimation(
                DelayAnimation(rootService.cableCar.AISpeed * 200).apply {
                    onFinished = {
                        BoardGameApplication.runOnGUIThread {
                            rootService.aIService.makeAIMove()
                        }
                    }
                }
            )

        }
    }
}