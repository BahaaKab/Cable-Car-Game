package view

import tools.aqua.bgw.components.ComponentView
import tools.aqua.bgw.components.gamecomponentviews.CardView
import tools.aqua.bgw.components.gamecomponentviews.TokenView
import tools.aqua.bgw.components.layoutviews.Pane
import tools.aqua.bgw.components.uicomponents.Button
import tools.aqua.bgw.components.uicomponents.Label
import tools.aqua.bgw.core.Alignment
import tools.aqua.bgw.util.Font
import tools.aqua.bgw.visual.ImageVisual
import tools.aqua.bgw.visual.Visual
import java.awt.Color
import javax.imageio.ImageIO

class ActivePlayerPane(posX: Int, posY: Int) :
    Pane<ComponentView>(posX, posY, 0, 0) {

    private val tile1Visual = ImageVisual(ImageIO.read(GameScene::class.java.getResource("/tile1.png")))
    private val player1ColorVisual = ImageVisual(ImageIO.read(GameScene::class.java.getResource("/color1.png")))

    private val rotateLeftVisual = ImageVisual(ImageIO.read(GameScene::class.java.getResource("/rotateLeft.png")))
    private val rotateRightVisual = ImageVisual(ImageIO.read(GameScene::class.java.getResource("/rotateRight.png")))
    private val drawTileVisual = ImageVisual(ImageIO.read(GameScene::class.java.getResource("/drawTile.png")))

    private val activePlayerTile = CardView(
        posX = 390, posY = 0,
        width = 240, height = 240,
        front = tile1Visual
    )

    init {
        addAll(
            TokenView(
                posX = 10, posY = 26,
                width = 70, height = 10,
                visual = player1ColorVisual
            ),

            Label(
                posX = 110, posY = 16,
                width = 100, height = 20,
                text = "Player 1",
                font = Font(
                    size = 20,
                    color = Color(5, 24, 156),
                    family = DEFAULT_FONT,
                    fontWeight = Font.FontWeight.SEMI_BOLD
                ),
                alignment = Alignment.CENTER_LEFT
            ),

            Label(
                posX = 110, posY = 41,
                width = 100, height = 20,
                text = "Score: 9",
                font = Font(
                    size = 20,
                    color = Color(5, 24, 156),
                    family = DEFAULT_FONT,
                    fontWeight = Font.FontWeight.SEMI_BOLD
                ),
                alignment = Alignment.CENTER_LEFT
            ),

            Button(
                posX = 0, posY = 116,
                width = 170, height = 40,
                text = "Rotate Left",
                font = Font(size = 20, color = Color.WHITE, family = DEFAULT_FONT, fontWeight = Font.FontWeight.BOLD),
                alignment = Alignment.CENTER_RIGHT,
                visual = Visual.EMPTY
            ).apply {
                componentStyle = "-fx-background-color: rgb(5,24,156);-fx-background-radius: 20px"
                onMouseClicked = {
                    activePlayerTile.rotation -= 90.0
                }
            },

            TokenView(
                posX = 10, posY = 124,
                width = 32, height = 27,
                visual = rotateLeftVisual
            ),

            Button(
                posX = 185, posY = 116,
                width = 183, height = 40,
                text = "Rotate Right",
                font = Font(size = 20, color = Color.WHITE, family = DEFAULT_FONT, fontWeight = Font.FontWeight.BOLD),
                alignment = Alignment.CENTER_RIGHT,
                visual = Visual.EMPTY
            ).apply {
                componentStyle = "-fx-background-color: rgb(5,24,156);-fx-background-radius: 20px"
                onMouseClicked = {
                    activePlayerTile.rotation += 90.0
                }
            },

            TokenView(
                posX = 195, posY = 124,
                width = 32, height = 27,
                visual = rotateRightVisual
            ),

            Button(
                posX = 112, posY = 176,
                width = 152, height = 40,
                text = "Draw Tile",
                font = Font(size = 20, color = Color.WHITE, family = DEFAULT_FONT, fontWeight = Font.FontWeight.BOLD),
                alignment = Alignment.CENTER_RIGHT,
                visual = Visual.EMPTY
            ).apply {
                componentStyle = "-fx-background-color: rgb(5,24,156);-fx-background-radius: 20px"
            },

            TokenView(
                posX = 123, posY = 183,
                width = 30, height = 30,
                visual = drawTileVisual
            ),

            activePlayerTile
        )
    }
}