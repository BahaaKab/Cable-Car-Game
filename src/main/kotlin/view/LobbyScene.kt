package view

import entity.PlayerType
import service.AssetsLoader
import tools.aqua.bgw.core.MenuScene
import service.RootService
import tools.aqua.bgw.components.uicomponents.Button
import tools.aqua.bgw.components.uicomponents.ComboBox
import tools.aqua.bgw.components.uicomponents.Label
import tools.aqua.bgw.core.Alignment
import tools.aqua.bgw.util.Font
import tools.aqua.bgw.visual.ColorVisual
import tools.aqua.bgw.visual.ImageVisual
import tools.aqua.bgw.visual.Visual
import view.components.CableCarLogo
import view.components.InputPlayerPane
import view.components.NumberOfPlayersPane
import view.components.PlayerIndicatorPane
import java.awt.Color
import kotlin.random.Random


/** LobbyScene shows the settings- & Player-Lobby in HotSeat- or Network-Mode.
 *
 * @param rootService A reference to the administration of the Game-State
 * @param isNetworkMode A boolean that defines which game mode the LobbyScene shows
 * @param yourName The name of the localPlayer if the scene shows a Network-Lobby
 * @param isHost A Boolean that shows if the local Player is the Host of a Network-Game
 * */
class LobbyScene(
    private val rootService: RootService,
    private var isNetworkMode: Boolean = false,
    var yourName: String = "",
    private var isHost: Boolean = false
) : MenuScene(1920, 1080), Refreshable {

    private var tileRotation = false
    private var playerNumber = 2
    private var changingPosition = -1

    private val cableCarLogo = CableCarLogo(810, 50).apply { scale = 1.1 }
    private val refreshArrowVisual = ImageVisual(AssetsLoader.refreshArrowGreyImage)
    private val refreshArrowBlue = ImageVisual(AssetsLoader.refreshArrowBlueImage)


    private val backArrow = Label(
        posX = 544, posY = 215,
        width = 30, height = 30,
        visual = ImageVisual(AssetsLoader.backArrowImage)
    ).apply { onMouseClicked = { leaveScene() } }

    private val backButton = Button(
        posX = 535,
        posY = 210,
        width = 110,
        height = 30,
        text = "Menu",
        alignment = Alignment.CENTER_RIGHT,
        visual = ColorVisual(249, 249, 250),
        font = Font(
            size = 21,
            color = Color.WHITE,
            family = DEFAULT_FONT_BOLD,
            fontWeight = Font.FontWeight.BOLD
        )
    ).apply {
        if (isNetworkMode) {
            posX -= 78
            width += 78
            text = "Leave Lobby"
            backArrow.posX -= 78
        }

        componentStyle = "-fx-background-color: rgba(233,233,236,1);-fx-background-radius: 100"
        onMouseClicked = { leaveScene() }
    }

    private val playerOrderButton = Button(
        posX = 670,
        posY = 210,
        width = 264,
        height = 30,
        text = "Shuffle Player Order",
        alignment = Alignment.CENTER_RIGHT,
        visual = ColorVisual(249, 249, 250),
        font = Font(
            size = 21,
            color = Color.WHITE,
            family = DEFAULT_FONT_BOLD,
            fontWeight = Font.FontWeight.BOLD
        )
    ).apply {
        componentStyle = "-fx-background-color: rgba(233,233,236,1);-fx-background-radius: 100"
        onMouseClicked = { randomOrder() }
    }

    private val cubePicture = Label(
        posX = 681, posY = 216,
        width = 28, height = 28,
        visual = ImageVisual(AssetsLoader.cubeImage)
    ).apply { onMouseClicked = { randomOrder() } }

    private val tileRotationButton = Button(
        posX = 964, posY = 210,
        width = 215, height = 30,
        text = "Enable Rotation",
        font = Font(
            size = 21, color = Color.WHITE, family = DEFAULT_FONT_BOLD,
            fontWeight = Font.FontWeight.BOLD
        ),
        alignment = Alignment.CENTER_RIGHT,
        visual = ColorVisual(249, 249, 250)
    ).apply {
        componentStyle = "-fx-background-color: rgba(233,233,236,1);-fx-background-radius: 100"
        onMouseClicked = { clickTileRotationButton() }

    }

    private val refreshArrow = Label(
        posX = 974, posY = 216,
        width = 28, height = 28,
        visual = refreshArrowVisual
    ).apply { onMouseClicked = { clickTileRotationButton() } }

    private val aISpeedBackground = Label(
        posX = 1200, posY = 210,
        width = 220, height = 36
    ).apply {
        componentStyle = "-fx-effect: dropshadow(one-pass-box, rgba(0,0,0,0.2), 10, 0, -1, 2);" +
                "-fx-background-radius: 80;-fx-background-color: rgba(255,255,255,1);"
    }

    private val aISpeedLabel = Label(
        posX = 1215, posY = 213,
        width = 100, height = 30,
        text = "AI-Speed:",
        font = Font(size = 21, color = DEFAULT_BLUE, family = DEFAULT_FONT_BOLD),
        alignment = Alignment.CENTER
    )

    private val aISpeedSetting = ComboBox(
        posX = 1315, posY = 210,
        width = 100, height = 40,
        font = Font(size = 21, color = DEFAULT_BLUE, family = DEFAULT_FONT_BOLD),
        items = listOf(0, 1, 2, 3)
    )

    // ------------------ PLAYER RELATED UI ELEMENTS -------------------
    private val colors = listOf(
        DEFAULT_YELLOW_COLOR, DEFAULT_BLUE_COLOR, DEFAULT_RED_COLOR,
        DEFAULT_GREEN_COLOR, DEFAULT_PURPLE_COLOR, DEFAULT_BLACK_COLOR
    )

    /** In single-player this creates the display in which you can choose the number of players.*/
    private val playerDisplay = List(5) { i ->
        NumberOfPlayersPane(555 + 125 * i, 290, i + 2)
    }


    private val playerInputs = playerInput()

    /* creates the "playerX:" and colorX labels. */
    private val playerIndicators = List(6) { i ->
        PlayerIndicatorPane(554, 375 + 90 * i, i + 1, colors[i])
    }

    private val orderButtons = orderButtons()

    private val startButton = Button(
        posX = 860, 950,
        width = 200, height = 50,
        text = "Start Game",
        font = Font(size = 23, color = Color.WHITE, family = DEFAULT_FONT_BOLD),
        alignment = Alignment.CENTER,
        visual = ColorVisual(249, 249, 250)
    ).apply {
        componentStyle = "-fx-background-color: rgba(5,24,156,1);-fx-background-radius: 100"
        onMouseClicked = {
            val playerInfos = playerInputs.map { it.getPlayerInfo() }
            if (playerNumber >= 2) {
                if (isNetworkMode) {
                    rootService.setupService.startNetworkGame(
                        isHost,
                        playerInfos.subList(0, playerNumber),
                        tileRotation,
                        null,
                        getAISpeed()
                    )
                } else {
                    rootService.setupService.startLocalGame(
                        playerInfos.subList(0, playerNumber), tileRotation, getAISpeed()
                    )
                }
            }

        }
    }

    private val sesIDReal = Label(
        posX = 710, posY = 280,
        width = 240, height = 40,
        alignment = Alignment.CENTER_LEFT,
        font = Font(size = 21, color = DEFAULT_BLUE, family = DEFAULT_FONT_MEDIUM),
        text = ""
    )

    private val secretReal = Label(
        posX = 1060, posY = 280,
        width = 270, height = 40,
        alignment = Alignment.CENTER_LEFT,
        font = Font(size = 21, color = DEFAULT_BLUE, family = DEFAULT_FONT_MEDIUM),
        text = rootService.networkService.secret
    )


    init {
        opacity = 1.0
        background = ColorVisual(247, 247, 247)

        addComponents(
            cableCarLogo, backButton, playerOrderButton, tileRotationButton, startButton,
            backArrow, cubePicture, refreshArrow, aISpeedBackground, aISpeedLabel, aISpeedSetting
        )

        for (i in playerIndicators.indices) {
            addComponents(playerIndicators[i], orderButtons[i])
        }

        if (isNetworkMode) {
            connectionLabel()
            for (input in playerInputs) {
                addComponents(input)
            }
            if (isHost) {
                playerInputs[0].changePlayerName(yourName)
                playerNumber = 1
            } else {
                startButton.isVisible = false
                tileRotationButton.isVisible = false
                playerOrderButton.isVisible = false
                refreshArrow.isVisible = false
                cubePicture.isVisible = false
                for (input in playerInputs){
                    input.deactivateSwitching()
                }
            }
        } else {
            for (display in playerDisplay) {
                addComponents(display)
            }
            for (input in playerInputs) {
                addComponents(input)
            }
            displayPlayers(2)
        }
    }

    /** This Method creates all buttons needed for switching the order of the players. */
    private fun orderButtons(): List<Button> {
        val mutList = mutableListOf<Button>()
        for (i in 1..6) {
            mutList.add(Button(
                posX = 1400, posY = 400 + 90 * (i - 1),
                width = 40, height = 40,
                text = "$i",
                font = Font(size = 21, color = Color.WHITE, family = DEFAULT_FONT_BOLD),
                visual = Visual.EMPTY
            ).apply {
                isVisible = false
                componentStyle = "-fx-background-color: rgba(5, 24, 156, 1);" +
                        "-fx-background-radius: 10"
                onMouseClicked = { changePlayersInput(i) }
            }
            )
        }
        return mutList.toList()
    }

    /** A method that creates the Input-Pane that displays the players names, kinds and order.*/
    private fun playerInput(): Array<InputPlayerPane> {
        val mutList = mutableListOf<InputPlayerPane>()
        mutList.add(InputPlayerPane(755, 375, 1, isNetworkMode, isHost))
        for (i in 2..6) {
            mutList.add(InputPlayerPane(755, 375 + 90 * (i - 1), i, isNetworkMode))
        }
        return mutList.toTypedArray()
    }

    /** In multiplayer this method displays the SessionID and the password.*/
    private fun connectionLabel() {
        val backgroundLabel = Label(
            posX = 570, posY = 270,
            width = 780, height = 60
        ).apply {
            componentStyle = "-fx-effect: dropshadow(one-pass-box, rgba(0,0,0,0.2), 10, 0, -1, 2);" +
                    "-fx-background-radius: 10;-fx-background-color: rgba(255,255,255,1);"
        }

        val sessionID = Label(
            posX = 590, posY = 280,
            width = 120, height = 40,
            alignment = Alignment.CENTER_LEFT,
            font = Font(
                size = 21, color = DEFAULT_BLUE, family = DEFAULT_FONT_BOLD,
                fontWeight = Font.FontWeight.BOLD
            ),
            text = "Session ID:"
        )

        val secretID = Label(
            posX = 980, posY = 280,
            width = 100, height = 40,
            alignment = Alignment.CENTER_LEFT,
            font = Font(
                size = 21, color = DEFAULT_BLUE, family = DEFAULT_FONT_BOLD,
                fontWeight = Font.FontWeight.BOLD
            ),
            text = "Secret:"
        )

        addComponents(backgroundLabel, sessionID, sesIDReal, secretID, secretReal)
    }

    /** A Method that changes the number of visible Input-Fields for players.*/
    fun displayPlayers(i: Int) {
        for (display in playerDisplay) {
            if (display.playerNumber in 3..i) {
                display.blueLine()
            } else {
                display.greyLine()
            }
        }

        for (k in playerInputs.indices) {
            playerInputs[k].isVisible = (k <= (i - 1))
            playerIndicators[k].isVisible = (k <= (i - 1))

            if (changingPosition != -1) {
                orderButtons[k].isVisible = (k <= (i - 1))
            }
        }

        playerNumber = i
    }

    /** This Method shows the Buttons that are able to push in the next move. */
    fun playerOrderOptions(paneNumber: Int) {
        if (paneNumber > playerNumber) {
            return
        }

        if (changingPosition == paneNumber) {
            changingPosition = -1

            for (buttons in orderButtons) {
                buttons.isVisible = false
            }
            return
        }

        changingPosition = paneNumber
        for (i in orderButtons.indices) {
            orderButtons[i].isVisible = (i < playerNumber)
            orderButtons[i].apply {
                componentStyle = "-fx-background-color: rgba(5, 24, 156, 1);-fx-background-radius: 10"
            }
        }
        orderButtons[(paneNumber - 1)].apply {
            componentStyle = "-fx-background-color: rgba($DEFAULT_GREY_STRING, 1);-fx-background-radius: 10"
        }
    }

    /** A Method that changes two selected InputPanes. */
    private fun changePlayersInput(newPos: Int) {

        for (button in orderButtons) {
            button.isVisible = false
        }

        if (newPos == changingPosition) {
            changingPosition = -1
            return
        }

        changePanes(newPos - 1, changingPosition - 1)

        if (!isNetworkMode) {
            for (i in playerInputs.indices) {
                playerInputs[i].isVisible = (i < playerNumber)
            }
        }
        changingPosition = -1
    }

    /** If a player clicks on the leave-Button/-Label this method changes the scene. */
    private fun leaveScene(){
        CableCarApplication.showMenuScene(CableCarApplication.chooseModeScene)
        this.refreshAfterGuestLeft(yourName)
        rootService.networkService.disconnect()
    }

    /** A Method that shuffles all InputPanes.*/
    private fun randomOrder() {

        for (i in 0 until playerNumber) {
            changePanes(i, Random.nextInt(i, playerNumber))
        }
    }

    /** If a player clicks on the Shuffle-Button/-Label this method shuffles the list of players.*/
    private fun clickTileRotationButton(){
        if (tileRotation) {
            tileRotationButton.componentStyle = "-fx-background-color: rgba(233,233,236,1);-fx-background-radius: 100"
            refreshArrow.visual = refreshArrowVisual
        } else {
            tileRotationButton.componentStyle = "-fx-background-color: rgba(5,24,156,1);-fx-background-radius: 100"
            refreshArrow.visual = refreshArrowBlue
        }
        tileRotation = tileRotation.not()
    }

    /** This Method switches two InputPlanes specified by two params that indicates the positions.
     *
     * @param pos1Pane Indicates the position of the first InputPlane (1.Pane = 0)
     * @param pos2Pane Indicates the position of the second InputPlane (2.Pane = 1)
     * */
    private fun changePanes(pos1Pane: Int, pos2Pane: Int) {
        playerInputs[pos1Pane].posY = (375 + 90 * (pos2Pane)) * 1.0
        playerInputs[pos1Pane].setOrderNumber(pos2Pane + 1)
        playerInputs[pos2Pane].posY = (375 + 90 * (pos1Pane)) * 1.0
        playerInputs[pos2Pane].setOrderNumber(pos1Pane + 1)

        val tmpInputPlayer = playerInputs[pos1Pane]
        playerInputs[pos1Pane] = playerInputs[pos2Pane]
        playerInputs[pos2Pane] = tmpInputPlayer
    }

    /** This gets the selected AI-Speed. */
    private fun getAISpeed(): Int {
        return if (aISpeedSetting.selectedItem == null) {
            1
        } else {
            aISpeedSetting.selectedItem!!
        }
    }

    /** Sets a new SessionID. */
    fun setSessionID(session: String) {
        sesIDReal.text = session
    }

     /** Hides this scene at the start of the game.*/
    override fun refreshAfterStartGame() {
        CableCarApplication.hideMenuScene()
    }

    /** After joining a Game the lobby refreshes all "Waiting..."-Labels with player-names.
     *
     * @param names The names of all players currently in network-lobby. Sorted by join-time.
     * */
    fun setAllPlayerNames(names: List<String>, localPlayerType: PlayerType) {
        for (i in names.indices) {
            playerInputs[i].changePlayerName(names[i])
            playerInputs[i].changeNetworkMode(true)
        }
        playerInputs[names.size].changePlayerName(yourName)
        playerInputs[names.size].setPlayerType(localPlayerType)
        playerInputs[names.size].changeNetworkMode(false)
        playerNumber = names.size + 1

    }

    /** After someone joined, his/her name will be added as new name to the showed list. */
    override fun refreshAfterGuestJoined(name: String) {
        if(isNetworkMode) {
            playerInputs[playerNumber].changePlayerName(name)
            playerNumber++
        }
    }

    /** Updates this lobby if a player left in a network-lobby.
     *
     * @param name The name of the player that left the lobby.
     * */
    override fun refreshAfterGuestLeft(name: String) {
        var whichPane = -1
        for(i in playerInputs.indices){
            if(playerInputs[i].getPlayerName() == name){
                whichPane = i
            }
        }
        if (whichPane < 0 || whichPane > 5 ){
            return
        }
        for(i in whichPane until playerNumber-1){
            changePanes(i, i+1)
        }
        playerInputs[playerNumber-1].changePlayerName("Waiting for Player...")
        playerNumber--
    }

    /** Sets the HostPlayer if this scene is shown in host-mode.*/
    fun setLocalPlayerName(name: String, localPlayerType: PlayerType) {
        playerInputs[0].playerName.text = name
        playerInputs[0].setPlayerType(localPlayerType)
        playerInputs[0].changeNetworkMode(false)
    }
}