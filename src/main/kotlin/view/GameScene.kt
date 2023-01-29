package view

import edu.udo.cs.sopra.ntf.TileInfo
import entity.GameMode
import entity.GameTile
import entity.PlayerType
import entity.StationTile
import service.AssetsLoader
import service.RootService
import tools.aqua.bgw.animation.DelayAnimation
import tools.aqua.bgw.animation.FadeAnimation
import tools.aqua.bgw.components.gamecomponentviews.CardView
import tools.aqua.bgw.components.gamecomponentviews.TokenView
import tools.aqua.bgw.components.layoutviews.GridPane
import tools.aqua.bgw.components.uicomponents.Label
import tools.aqua.bgw.core.BoardGameApplication
import tools.aqua.bgw.core.BoardGameScene
import tools.aqua.bgw.util.BidirectionalMap
import tools.aqua.bgw.util.Font
import tools.aqua.bgw.visual.ImageVisual
import tools.aqua.bgw.visual.Visual
import view.components.ActivePlayerPane
import view.components.CableCarLogo
import view.components.OptionsPane
import view.components.OtherPlayersPane
import java.awt.BasicStroke
import java.awt.Color
import java.awt.image.BufferedImage


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
    private val boardVisual = ImageVisual(AssetsLoader.boardImage)

    private val stationTileMap = BidirectionalMap<StationTile, CardView>()

    private val logoPane = CableCarLogo(posX = 270, posY = 104)

    private val optionsPane = OptionsPane(rootService = rootService, posX = 90, posY = 256)

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
                back = DEFAULT_BACKGROUND_COLOR.copy().apply {
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

    private val dummyTiles = List(8) { column ->
        List(8) { row ->
            TokenView(
                posX = board.posX + 100 * (column + 1),
                posY = board.posY + 100 * (row + 1),
                width = 100,
                height = 100,
                visual = Visual.EMPTY
            ).apply {
                if(!((column == 3 || column == 4) && (row == 3 || row == 4))) {
                    onMouseClicked = {
                        rootService.playerActionService.placeTile(posX = column + 1, posY = row + 1) }
                }
            }
        }
    }

    private val drawImage = BufferedImage(1000, 1000, BufferedImage.TYPE_INT_ARGB)

    private val paintBrush = drawImage.createGraphics().apply {
        background = Color(0,0,0,0)
        color = Color.BLACK
        stroke = BasicStroke(5.0f)
    }

    private val pathImage = TokenView(
        posX = board.posX,
        posY = board.posY,
        width = board.width,
        height = board.height,
        visual = ImageVisual(drawImage)
    )


    init {
        background = DEFAULT_BACKGROUND_COLOR
        addComponents(
            logoPane,
            optionsPane,
            activePlayerPane,
            otherPlayersPane,
            connectionStatusLabel,
            board,
            pathImage
        )
        for (tileColumn in dummyTiles){
            for(tile in tileColumn) {
                addComponents(tile)
            }
        }
    }

    /** Initializes the Station Tiles as graphics. */
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

    /** Initializes a Station Tile as graphic. */
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

        val playerNumber = rootService.cableCar.currentState.players.size
        if( playerNumber in 5..6 || playerNumber == 3){
            paintBrush.color = Color(230, 230, 233)

            paintBrush.drawLine(909, 874, 966, 874)
            paintBrush.drawLine(967, 863, 967, 885)
            paintBrush.drawLine(841, 909, 841, 966)
            paintBrush.drawLine(830, 967, 852, 967)

            pathImage.visual = ImageVisual(drawImage)
        }
    }

    /** Refreshes the board after Undo/Redo. */
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
     * Configure the board after the start of the Game.
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
        optionsPane.setAISpeed(rootService.cableCar.AISpeed)
        refreshAfterNextTurn()
    }

    /**
     * Rotates a Tile leftwards.
     */
    override fun refreshAfterRotateTileLeft() {
        activePlayerPane.activePlayerTiles.last().rotate(-90)
        showPlaceablePositions()
    }

    /**
     * Rotates a Tile rightwards.
     */
    override fun refreshAfterRotateTileRight() {
        activePlayerPane.activePlayerTiles.last().rotate(90)
        showPlaceablePositions()
    }

    /**
     * Refreshes the boards and Panes after Undo.
     */
    override fun refreshAfterUndo(oldState: entity.State) {
        refreshBoard(oldState)
        activePlayerPane.refreshActivePlayer()
        otherPlayersPane.refreshOtherPlayers()
        showPlaceablePositions()
    }

    /**
     * Refreshes the boards and Panes after Redo.
     */
    override fun refreshAfterRedo(oldState: entity.State) = refreshAfterUndo(oldState)

    /**
     * Places a tile on the board.
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
     * Refreshes the shown handcards after drawing a tile.
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
        showPlaceablePositions()
    }

    /**
     * Refreshes after a Turn.
     */
    override fun refreshAfterNextTurn() {
        if(!rootService.cableCar.currentState.activePlayer.isNetworkPlayer) showPlaceablePositions()
        else hidePlaceablePositions()
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
        } else {
            if(rootService.cableCar.allowTileRotation) activePlayerPane.enableTileRotationButtons()
            unlock()
        }

        activePlayerPane.refreshActivePlayer()
        otherPlayersPane.refreshOtherPlayers()

        startAIHandler()
    }

    /** Shows all placeable position for the currentTile.*/
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

    /** Hides Alle placeable Positions.*/
    private fun hidePlaceablePositions() = placeablePositions.forEach { (x,y) ->
        board[x, y]?.showFront()
    }

    /** Starts the AI Handler if needed.*/
    private fun startAIHandler() {
        if (rootService.cableCar.currentState.activePlayer.playerType in listOf(
                PlayerType.AI_EASY,
                PlayerType.AI_HARD
            )
        ) {
            activePlayerPane.disableDrawTileButton()
            activePlayerPane.disableTileRotationButtons()
            optionsPane.disableUndoRedo()
            hidePlaceablePositions()

            playAnimation(
                DelayAnimation(rootService.cableCar.AISpeed * 1000).apply {
                    onFinished = {
                        BoardGameApplication.runOnGUIThread {
                            rootService.aIService.makeAIMove()
                        }
                    }
                }
            )

        }
    }

    /** Resets the image in which the paths are drawn.*/
    fun resetImage(){
        paintBrush.clearRect(0,0, drawImage.width, drawImage.height)
    }

    /** After a Tile is placed all paths related to this Tile are drawn. */
    override fun refreshAfterPathElementUpdated(x: Int, y: Int, connectionA: Int,
                                                connectionB: Int, color: entity.Color) {

        // Set color of the connection:
        setPaintBrush(color)

        val cornerTileFactor = (0.166 * 100).toInt()
        if(connectionA == 0 && connectionB == 1){
           // paintBrush.drawLine(conAPos[0],conAPos[1],conAPos[0], conAPos[1] + ((0.166 * 100)).toInt())
           paintBrush.drawLine(x * 101 + 33,y * 101, x * 101 + 33,y * 101 + cornerTileFactor)
           paintBrush.drawLine(x * 101 + 66,y * 101, x * 101 + 66,y * 101 + cornerTileFactor)
           paintBrush.drawLine(x * 101 + 33,y * 101 + cornerTileFactor, x * 101 + 66,y * 101 + cornerTileFactor)
        } else if(connectionA == 2 && connectionB == 3){
            paintBrush.drawLine((x+1) * 101 - cornerTileFactor,y * 101 + 33, (x+1) * 101 ,y * 101 + 33)
            paintBrush.drawLine((x+1) * 101 - cornerTileFactor,y * 101 + 66, (x+1) * 101 ,y * 101 + 66)
            paintBrush.drawLine((x+1) * 101 - cornerTileFactor,y * 101 + 33, (x+1) * 101 - cornerTileFactor,y * 101 + 66)
        } else if(connectionA == 4 && connectionB == 5){
            paintBrush.drawLine(x * 101 + 33,(y+1) * 101, x * 101 + 33,(y+1) * 101 - cornerTileFactor)
            paintBrush.drawLine(x * 101 + 66,(y+1) * 101, x * 101 + 66,(y+1) * 101 - cornerTileFactor)
            paintBrush.drawLine(x * 101 + 33,(y+1) * 101 - cornerTileFactor, x * 101 + 66,(y+1) * 101 - cornerTileFactor)
        } else if(connectionA == 6 && connectionB == 7){
            paintBrush.drawLine(x * 101,y * 101 + 33, x * 101 + cornerTileFactor,y * 101 + 33)
            paintBrush.drawLine(x * 101,y * 101 + 66, x * 101 + cornerTileFactor,y * 101 + 66)
            paintBrush.drawLine(x * 101 + cornerTileFactor,y * 101 + 33, x * 101 + cornerTileFactor,y * 101 + 66)
        }else{
            val conAPos = getPosition(connectionA)
            val conBPos = getPosition(connectionB)

            val failurePos = intArrayOf(-1,-1)
            if(conAPos.contentEquals(failurePos) || conBPos.contentEquals(failurePos)){
                println("ERROR: could not identify Connections")
                return
            }

            conAPos[0] = conAPos[0] * 33
            conAPos[1] = conAPos[1] * 33

            conBPos[0] = conBPos[0] * 33
            conBPos[1] = conBPos[1] * 33

            paintBrush.drawLine(x * 101 + conAPos[0],y * 101 + conAPos[1],
                                x * 101 + conBPos[0],y * 101 + conBPos[1])
        }

        pathImage.visual = ImageVisual(drawImage)
    }

    /** If the last Tile of the path is a power-station or normal station-Tile
     *  this method draws it.*/
    override fun refreshPathAfterEndTile(x: Int, y: Int, connector: Int, color: entity.Color) {
        setPaintBrush(color)

        if( x == 0 ){
            paintBrush.drawLine(42, y * 101 + 33, 101, y * 101 + 33)
            paintBrush.drawLine(41, y * 101 + 22, 41 , y * 101 + 44)
        }else if( y == 0 ){
            paintBrush.drawLine(x * 101 + 66, 42, x * 101 + 66, 101)
            paintBrush.drawLine(x * 101 + 55, 41, x * 101 + 77, 41)
        }else if( x == 9 ){
            paintBrush.drawLine(909, y * 101 + 66, 966,y * 101 + 66)
            paintBrush.drawLine(967, y * 101 + 55, 967, y * 101 + 77)
        }else if( y == 9 ){
            paintBrush.drawLine(x * 101 + 33, 909, x * 101 + 33, 966 )
            paintBrush.drawLine(x * 101 + 22, 967, x * 101 + 44, 967)
        }else if( (x == 5 || x == 4) && (y == 4 || y == 5)){
            val conPos = getPosition(connector)

            // inverted Position because of the power Tile modeling
            if(conPos[0] == 0){
                paintBrush.drawLine((x+1) * 101 - 20, y * 101 + conPos[1] * 33,
                    (x+1) * 101, y * 101 + conPos[1] * 33)
            }else if (conPos[0] == 3){
                paintBrush.drawLine(x * 101, y * 101 + conPos[1] * 33,
                    x * 101 + 20, y * 101 + conPos[1] * 33)
            }else if(conPos[1] == 0){
                paintBrush.drawLine(x * 101 + conPos[0] * 33, (y+1) * 101 - 20,
                    x * 101 + conPos[0] * 33, (y+1) * 101)
            }else if(conPos[1] == 3){
                paintBrush.drawLine(x * 101 + conPos[0] * 33, y * 101 ,
                    x * 101 + conPos[0] * 33, y * 101 + 20)
            }
        }

        pathImage.visual = ImageVisual(drawImage)
    }

    /** Sets the color of the drawing paint brush.*/
    private fun setPaintBrush(color: entity.Color){
        paintBrush.color = when (color) {
            entity.Color.YELLOW -> DEFAULT_YELLOW_COLOR
            entity.Color.BLUE -> DEFAULT_BLUE_COLOR
            entity.Color.ORANGE -> DEFAULT_RED_COLOR
            entity.Color.GREEN -> DEFAULT_GREEN_COLOR
            entity.Color.PURPLE -> DEFAULT_PURPLE_COLOR
            entity.Color.BLACK -> DEFAULT_BLACK_COLOR
        }.color
    }

    /** Returns the position of a given connector / ConnectionPoint.
     * @return [-1,-1] if the connector is not valid.*/
    private fun getPosition(i: Int)  = when (i) {
        0 -> intArrayOf(1, 0)
        1 -> intArrayOf(2, 0)
        2 -> intArrayOf(3, 1)
        3 -> intArrayOf(3, 2)
        4 -> intArrayOf(2, 3)
        5 -> intArrayOf(1, 3)
        6 -> intArrayOf(0, 2)
        7 -> intArrayOf(0, 1)
        else -> intArrayOf(-1, -1)
    }
}