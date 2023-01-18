package view

import entity.PlayerInfo
import entity.PLAYER_ORDER_COLORS
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
import view.components.PlayerIndicatorPane
import java.awt.Color
import javax.imageio.ImageIO


/** LobbyScene shows the settings- & Player-Lobby in HotSeat- or Multiplayer-Mode.
 *
 * @param rootService A reference to the administration of the Game-State
 * @param isNetworkMode A boolean that defines which game mode the LobbyScene shows
 * @param hostName The name of the hostPlayer if the scene shows a Multiplayer-Lobby*/
class LobbyScene(private val rootService: RootService, private val isNetworkMode : Boolean = false,
                 private val hostName : String = "") : MenuScene(1920, 1080), Refreshable {

    private var tileRotation = false

    private var playerNumber = 2

    private val cableCarLogo = CableCarLogo(810,50).apply { scale = 1.1 }

    private val refreshArrowVisual = ImageVisual(ImageIO.read(LobbyScene::class.java.getResource("/arrow_refresh.png")))

    private val rotateRightArrow = ImageVisual(ImageIO.read(LobbyScene::class.java.getResource("/rotateRight.PNG")))

    private val backArrow = Label(
        posX = 644, posY = 215,
        width = 30, height = 30,
        visual = ImageVisual(ImageIO.read(LobbyScene::class.java.getResource("/arrow.PNG")))
    )

    private val backButton = backButton()

    private val playerOrderButton = Button(
        posX = 770, posY = 210,
        width = 264, height = 30,
        text = "Shuffle Player Order",
        font = Font(size = 21, color = Color.WHITE, family = DEFAULT_FONT_BOLD,
            fontWeight = Font.FontWeight.BOLD),
        alignment = Alignment.CENTER_RIGHT,
        visual = ColorVisual(249, 249, 250)
    ).apply { componentStyle = "-fx-background-color: rgba(233,233,236,1);-fx-background-radius: 100" }

    private val cubePicture = Label(
        posX = 781, posY = 216,
        width = 28, height = 28,
        visual = ImageVisual(ImageIO.read(LobbyScene::class.java.getResource("/cube.png")))
    )

    private val tileRotationButton = Button(
        posX = 1064, posY = 210,
        width = 215, height = 30,
        text = "Enable Rotation",
        font = Font(size = 21, color = Color.WHITE, family = DEFAULT_FONT_BOLD,
            fontWeight = Font.FontWeight.BOLD),
        alignment = Alignment.CENTER_RIGHT,
        visual = ColorVisual(249, 249, 250)
    ).apply { componentStyle = "-fx-background-color: rgba(233,233,236,1);-fx-background-radius: 100"
        onMouseClicked = {
            if(tileRotation){
                componentStyle ="-fx-background-color: rgba(233,233,236,1);-fx-background-radius: 100"
                refreshArrow.visual = refreshArrowVisual
            }else{
                componentStyle ="-fx-background-color: rgba(5,24,156,1);-fx-background-radius: 100"
                refreshArrow.visual = rotateRightArrow
            }
            tileRotation = tileRotation.not()
            println(tileRotation)
        }
    }

    private val refreshArrow = Label(
        posX = 1074, posY = 216,
        width = 28, height = 28,
        visual = refreshArrowVisual
    )

    private val playerDisplay = playerDisplay()

    private val colors = listOf(DEFAULT_YELLOW_COLOR, DEFAULT_BLUE_COLOR, DEFAULT_RED_COLOR,
                                DEFAULT_GREEN_COLOR, DEFAULT_PURPLE_COLOR, DEFAULT_BLACK_COLOR)

    private val playerInputs = playerInput()

    private val playerIndicators = playerIndicator()

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
            if(isNetworkMode) {
                // Todo
            } else {

                val playerInfos = playerInputs.mapIndexed { i, playerInputPane ->
                    val name = checkPlayerName(playerInputPane.getTextFieldInput(), i)
                    val playerType = playerInputPane.getPlayerType()
                    val color = PLAYER_ORDER_COLORS[i]
                    PlayerInfo(name, playerType, color, false)
                }
                // TODO: make tileRotation and AISpeed choosable
                rootService.setupService.startLocalGame(playerInfos, true, 1)
            }
        }
    }


    init {
        opacity = 1.0
        background = ColorVisual(247, 247, 247)

        addComponents(cableCarLogo , backButton, playerOrderButton, tileRotationButton, startButton,
                      backArrow, cubePicture, refreshArrow)

        for (playerIndicator in playerIndicators ){
            addComponents(playerIndicator)
        }

        if(isNetworkMode){
            connectionLabel()
            for (input in playerInputs) {
                addComponents(input)
            }
            playerInputs[0].changePlayerName(hostName)
        }else {
            for (display in playerDisplay) {
                addComponents(display)
            }
            for (input in playerInputs) {
                addComponents(input)
            }
        }

        displayPlayers(2)
    }

    /**This method creates the button to go back to the Scene before, based on the game mode. */
    private fun backButton() : Button {
        var i = 0
        var name = "Menu"
        if(isNetworkMode){
            i = 78
            name = "Leave Lobby"
        }

        backArrow.posX = (backArrow.posX - i)

        return Button(
                posX = (635-i), posY = 210,
                width = (110+i), height = 30,
                text = name,
                font = Font(size = 21, color = Color.WHITE, family = DEFAULT_FONT_BOLD,
                    fontWeight = Font.FontWeight.BOLD),
                alignment = Alignment.CENTER_RIGHT,
                visual = ColorVisual(249, 249, 250)
            ).apply { componentStyle = "-fx-background-color: rgba(233,233,236,1);-fx-background-radius: 100" }
    }

    /** In single-player this method creates the display in which you can choose the number of players.*/
    private fun playerDisplay() : List<NumberOfPlayersPane>{
        val mutList = mutableListOf<NumberOfPlayersPane>()
        for(i in 2 .. 6) {
            mutList.add(NumberOfPlayersPane( 555 + 125 * (i-2), 290, i))
        }
        return mutList.toList()
    }

    private fun playerIndicator() : List<PlayerIndicatorPane>{
        val mutList = mutableListOf<PlayerIndicatorPane>()
        for(i in 1 .. 6) {
            mutList.add(PlayerIndicatorPane(554, 375 + 90 * (i-1), i, colors[i-1]))
        }
        return mutList.toList()
    }

    /** A method that creates the Input-Pane that displays the players names, kinds and order.*/
    private fun playerInput() : List<InputPlayerPane>{
        val mutList = mutableListOf<InputPlayerPane>()
        mutList.add(InputPlayerPane(755, 375 ,  isNetworkMode, true))
        for(i in 2 .. 6) {
            mutList.add(InputPlayerPane(755, 375 + 90 * (i-1), isNetworkMode))
        }
        return mutList.toList()
    }

    /**In multiplayer this method displays the SessionID and the password.*/
    private fun connectionLabel(){
        val backgroundLabel = Label(
            posX = 570, posY = 270,
            width = 780, height = 60
            ).apply { componentStyle = "-fx-effect: dropshadow(one-pass-box, rgba(0,0,0,0.2), 10, 0, -1, 2);" +
                "-fx-background-radius: 10;-fx-background-color: rgba(255,255,255,1);"}

        val sessionID = Label(
            posX = 590, posY = 280,
            width = 120, height = 40,
            alignment = Alignment.CENTER_LEFT,
            font = Font(size = 21, color = DEFAULT_BLUE , family = DEFAULT_FONT_BOLD,
                        fontWeight = Font.FontWeight.BOLD),
            text = "Session ID:"
        )

        val sesIDReal = Label(
            posX = 710, posY = 280,
            width = 240, height = 40,
            alignment = Alignment.CENTER_LEFT,
            font = Font(size = 21, color = DEFAULT_BLUE , family = DEFAULT_FONT_MEDIUM),
            text = "thisIsOurFavouriteGame"
        )

        val secretID = Label(
            posX = 980, posY = 280,
            width = 100, height = 40,
            alignment = Alignment.CENTER_LEFT,
            font = Font(size = 21, color = DEFAULT_BLUE , family = DEFAULT_FONT_BOLD,
                        fontWeight = Font.FontWeight.BOLD),
            text = "Secret:"
        )

        val secretReal = Label(
            posX = 1060, posY = 280,
            width = 270, height = 40,
            alignment = Alignment.CENTER_LEFT,
            font = Font(size = 21, color = DEFAULT_BLUE , family = DEFAULT_FONT_MEDIUM),
            text = "AMIN4PRESIDENT"
        )

        addComponents(backgroundLabel, sessionID, sesIDReal, secretID, secretReal)
    }

    private fun checkPlayerName(name : String, i : Int) : String{
        return if(name == ""){
            "Player $i"
        }else{
            name
        }
    }

    fun displayPlayers(i : Int){
        for(display in playerDisplay){
            if(display.playerNumber in 3.. i){
                display.blueLine()
            }else{
                display.greyLine()
            }
        }

        for(k in playerInputs.indices){
            playerInputs[k].isVisible = (k <= (i-1))
            playerIndicators[k].isVisible = (k <= (i-1))
        }

        playerNumber = i
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

    /**
     * @see view.Refreshable.refreshAfterJoinGame
     */
    override fun refreshAfterJoinGame(names: List<String>) {
    }
}