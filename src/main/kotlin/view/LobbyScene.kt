package view

import entity.PlayerInfo
import entity.PLAYER_ORDER_COLORS
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
import javax.imageio.ImageIO
import kotlin.random.Random


/** LobbyScene shows the settings- & Player-Lobby in HotSeat- or Multiplayer-Mode.
 *
 * @param rootService A reference to the administration of the Game-State
 * @param isNetworkMode A boolean that defines which game mode the LobbyScene shows
 * @param yourName The name of the hostPlayer if the scene shows a Multiplayer-Lobby
 * @param isHost A Boolean that shows if the local Player is the Host of a Network-Game
 * */
class LobbyScene(private val rootService: RootService, private val isNetworkMode : Boolean = false,
                 private val yourName: String = "", isHost : Boolean = false)
            : MenuScene(1920, 1080), Refreshable {

    private var tileRotation = false
    private var playerNumber = 2
    private var changingPosition = -1

    private val cableCarLogo = CableCarLogo(810, 50).apply { scale = 1.1 }
    private val refreshArrowVisual = ImageVisual(ImageIO.read(LobbyScene::class.java.getResource("/arrow_refresh.png")))
    private val refreshArrowBlue = ImageVisual(ImageIO.read(LobbyScene::class.java.getResource("/arrow_refresh_blue.png")))

    private val backArrow = Label(
        posX = 544, posY = 215,
        width = 30, height = 30,
        visual = ImageVisual(ImageIO.read(LobbyScene::class.java.getResource("/arrow.PNG")))
    )

    private val backButton = backButton()

    private val playerOrderButton = Button(
        posX = 670, posY = 210,
        width = 264, height = 30,
        text = "Shuffle Player Order",
        font = Font(
            size = 21, color = Color.WHITE, family = DEFAULT_FONT_BOLD,
            fontWeight = Font.FontWeight.BOLD
        ),
        alignment = Alignment.CENTER_RIGHT,
        visual = ColorVisual(249, 249, 250)
    ).apply { componentStyle = "-fx-background-color: rgba(233,233,236,1);-fx-background-radius: 100"
        onMouseClicked = { randomOrder() }
    }

    private val cubePicture = Label(
        posX = 681, posY = 216,
        width = 28, height = 28,
        visual = ImageVisual(ImageIO.read(LobbyScene::class.java.getResource("/cube.png")))
    )

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
    ).apply { componentStyle = "-fx-background-color: rgba(233,233,236,1);-fx-background-radius: 100"
        onMouseClicked = {

            if (tileRotation) {
                componentStyle = "-fx-background-color: rgba(233,233,236,1);-fx-background-radius: 100"
                refreshArrow.visual = refreshArrowVisual
            } else {
                componentStyle = "-fx-background-color: rgba(5,24,156,1);-fx-background-radius: 100"
                refreshArrow.visual = refreshArrowBlue
            }
            tileRotation = tileRotation.not()
        }

    }

    private val refreshArrow = Label(
        posX = 974, posY = 216,
        width = 28, height = 28,
        visual = refreshArrowVisual
    )

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
        items = listOf(0,1,2,3,4,5)
    )

    private val playerDisplay = playerDisplay()

    private val colors = listOf(
        DEFAULT_YELLOW_COLOR, DEFAULT_BLUE_COLOR, DEFAULT_RED_COLOR,
        DEFAULT_GREEN_COLOR, DEFAULT_PURPLE_COLOR, DEFAULT_BLACK_COLOR
    )

    private val playerInputs = playerInput()

    private val playerIndicators = playerIndicator()

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
            if (playerNumber >= 2) {
                if (isNetworkMode) {
                    rootService.setupService.startNetworkGame(
                        true, createPlayerInfos(true).subList(0, playerNumber),
                        tileRotation, null, getAISpeed()
                    )
                } else {
                    rootService.setupService.startLocalGame(
                        createPlayerInfos(false).subList(0, playerNumber), tileRotation, getAISpeed()
                    )
                }
            }

        }
    }

    private val sesIDReal = Label(
        posX = 710, posY = 280,
       width = 240, height = 40,
        alignment = Alignment.CENTER_LEFT,
        font = Font(size = 21, color = DEFAULT_BLUE , family = DEFAULT_FONT_MEDIUM),
        text = "thisIsOurFavouriteGame"
    )

    private val secretReal = Label(
        posX = 1060, posY = 280,
        width = 270, height = 40,
        alignment = Alignment.CENTER_LEFT,
        font = Font(size = 21, color = DEFAULT_BLUE , family = DEFAULT_FONT_MEDIUM),
        text = "AMIN4PRESIDENT"
    )


    init {
        opacity = 1.0
        background = ColorVisual(247, 247, 247)

        addComponents(
            cableCarLogo, backButton, playerOrderButton, tileRotationButton, startButton,
            backArrow, cubePicture, refreshArrow, aISpeedBackground, aISpeedLabel, aISpeedSetting
        )

        for (i in playerIndicators.indices){
            addComponents(playerIndicators[i], orderButtons[i])
        }

        if (isNetworkMode) {
            connectionLabel()
            for (input in playerInputs) {
                addComponents(input)
            }
            if(isHost) {
                playerInputs[0].changePlayerName(yourName)
                playerNumber = 1
            } else {
                startButton.isDisabled = true
                tileRotationButton.isDisabled = true
                playerOrderButton.isDisabled = true
                for(playerInput in playerInputs){
                    playerInput.deactivateKick()
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

    /** This method creates the button to go back to the Scene before, based on the game mode. */
    private fun backButton(): Button {
        var i = 0
        var name = "Menu"
        if (isNetworkMode) {
            i = 78
            name = "Leave Lobby"
        }

        backArrow.posX = (backArrow.posX - i)

        return Button(
                posX = (535-i), posY = 210,
                width = (110+i), height = 30,
                text = name,
                font = Font(
                    size = 21, color = Color.WHITE, family = DEFAULT_FONT_BOLD,
                    fontWeight = Font.FontWeight.BOLD
                ),
                alignment = Alignment.CENTER_RIGHT,
                visual = ColorVisual(249, 249, 250)
            ).apply { componentStyle = "-fx-background-color: rgba(233,233,236,1);-fx-background-radius: 100"
                    onMouseClicked = { CableCarApplication.showMenuScene(CableCarApplication.chooseModeScene) }
            }
    }

    /** In single-player this method creates the display in which you can choose the number of players.*/
    private fun playerDisplay(): List<NumberOfPlayersPane> {
        val mutList = mutableListOf<NumberOfPlayersPane>()
        for (i in 2..6) {
            mutList.add(NumberOfPlayersPane(555 + 125 * (i - 2), 290, i))
        }
        return mutList.toList()
    }

    /** A Method which creates the "playerX:" and colorX labels.*/
    private fun playerIndicator(): List<PlayerIndicatorPane>{
        val mutList = mutableListOf<PlayerIndicatorPane>()
        for(i in 1 .. 6) {
            mutList.add(PlayerIndicatorPane(554, 375 + 90 * (i-1), i, colors[i-1]))
        }
        return mutList.toList()
    }

    /** This Method creates all buttons needed for switching the order of the players. */
    private fun orderButtons(): List<Button>{
        val mutList = mutableListOf<Button>()
        for(i in 1 .. 6) {
            mutList.add(Button(
                posX = 1400, posY = 400 + 90 *(i-1),
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
    private fun playerInput(): Array<InputPlayerPane>{
        val mutList = mutableListOf<InputPlayerPane>()
        mutList.add(InputPlayerPane(755, 375, 1, isNetworkMode, true))
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

    /** A Method that checks if a player-name is not empty. */
    private fun checkPlayerName(name : String, i : Int) : String {
        return if(name == ""){
            "Player $i"
        }else{
            name
        }
    }

    /** A Method that changes the number of visible Input-Fields for players.*/
    fun displayPlayers(i : Int) {
        for (display in playerDisplay){
            if(display.playerNumber in 3.. i){
                display.blueLine()
            }else{
                display.greyLine()
            }
        }

        for (k in playerInputs.indices){
            playerInputs[k].isVisible = (k <= (i-1))
            playerIndicators[k].isVisible = (k <= (i-1))

            if(changingPosition != -1){
                orderButtons[k].isVisible = (k <= (i-1))
            }
        }

        playerNumber = i
    }

    /** This Method shows the Buttons that are able to push in the next move. */
    fun playerOrderOptions(paneNumber : Int) {
        if (paneNumber > playerNumber){
            return
        }

        if(changingPosition == paneNumber){
            changingPosition = -1

            for(buttons in orderButtons){
                buttons.isVisible = false
            }
            return
        }

        changingPosition = paneNumber
        for (i in orderButtons.indices){
            orderButtons[i].isVisible = (i < playerNumber)
            orderButtons[i].apply {
                componentStyle = "-fx-background-color: rgba(5, 24, 156, 1);-fx-background-radius: 10"
            }
        }
        orderButtons[(paneNumber-1)].apply {
            componentStyle = "-fx-background-color: rgba($DEFAULT_GREY_STRING, 1);-fx-background-radius: 10"
        }
    }

    /** A Method that changes two selected InputPanes. */
    private fun changePlayersInput(newPos : Int){

        for (button in orderButtons){
            button.isVisible = false
        }

        if(newPos == changingPosition){
            changingPosition = -1
            return
        }

        changePanes(newPos-1 , changingPosition-1)

        if(!isNetworkMode) {
            for (i in playerInputs.indices) {
                playerInputs[i].isVisible = (i < playerNumber)
            }
        }
        changingPosition = -1
    }

    /** A Method that shuffles all InputPanes.*/
    private fun randomOrder(){

        for(i in 0 until playerNumber){
            changePanes(i, Random.nextInt(i, playerNumber))
        }
    }

    /** This Method switches two InputPlanes specified by two params that indicates the postions.
     *
     * @param pos1Pane Indicates the position of the first InputPlane (1.Pane = 1)
     * @param pos2Pane Indicates the position of the second InputPlane (2.Pane = 2)
     * */
    private fun changePanes(pos1Pane : Int, pos2Pane : Int){
        playerInputs[pos1Pane].posY = (375 + 90 * (pos2Pane)) * 1.0
        playerInputs[pos1Pane].setOrderNumber(pos2Pane+1)
        playerInputs[pos2Pane].posY = (375 + 90 * (pos1Pane)) * 1.0
        playerInputs[pos2Pane].setOrderNumber(pos1Pane+1)

        val tmpInputPlayer = playerInputs[pos1Pane]
        playerInputs[pos1Pane] = playerInputs[pos2Pane]
        playerInputs[pos2Pane] = tmpInputPlayer
    }

    /** Create a list of playerInfos for all possible Players to give it the setup Service */
    private fun createPlayerInfos(isNetwork : Boolean) : List<PlayerInfo> {
        val playerInfos = playerInputs.mapIndexed { i, playerInputPane ->
            val name = checkPlayerName(playerInputPane.getTextFieldInput(), i)
            val playerType = playerInputPane.getPlayerType()
            val color = PLAYER_ORDER_COLORS[i]
            PlayerInfo(name, playerType, color, isNetwork)
        }
        return playerInfos
    }

    /** This gets the selected AI-Speed. */
    private fun getAISpeed() : Int {
        return if(aISpeedSetting.selectedItem == null){
            1
        }else{
            aISpeedSetting.selectedItem!!
        }
    }

    /** Sets a new SessionID. */
    fun setSessionID(session : String){
        sesIDReal.text = session
    }

    /** Sets a new secret. */
    fun setSecret(secret : String){
        secretReal.text = secret
    }


    /**
     * @see view.Refreshable.refreshAfterStartGame
     */
    override fun refreshAfterStartGame() {
        CableCarApplication.hideMenuScene()
    }

    /**
     * @see view.Refreshable.refreshAfterHostGame
     */
    override fun refreshAfterHostGame() {
    }

    /** After joining a Game the lobby refreshes all "Waiting..."-Labels with player-names.*/
    override fun refreshAfterJoinGame(names: List<String>) {
        for(i in names.indices){
            playerInputs[i].changePlayerName(names[i])
        }
        playerInputs[names.size].changePlayerName(yourName)
        playerNumber = names.size+1
    }

    /** After someone joined, his/her name will be added as new name to the showed list. */
    override fun refreshAfterGuestJoined(name: String) {
        playerInputs[playerNumber].changePlayerName(name)
        playerNumber+1
    }
}