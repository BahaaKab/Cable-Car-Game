package view

import tools.aqua.bgw.components.ComponentView
import tools.aqua.bgw.components.gamecomponentviews.CardView
import tools.aqua.bgw.components.gamecomponentviews.TokenView
import tools.aqua.bgw.components.layoutviews.Pane
import tools.aqua.bgw.components.uicomponents.Label
import tools.aqua.bgw.core.Alignment
import tools.aqua.bgw.util.Font
import tools.aqua.bgw.visual.ImageVisual
import java.awt.Color
import javax.imageio.ImageIO

class OtherPlayersPane(posX: Int, posY: Int) :
    Pane<ComponentView>(posX, posY, 0, 0) {

    private val tile2Visual = ImageVisual(ImageIO.read(GameScene::class.java.getResource("/tile2.png")))
    private val tile3Visual = ImageVisual(ImageIO.read(GameScene::class.java.getResource("/tile3.png")))
    private val tile4Visual = ImageVisual(ImageIO.read(GameScene::class.java.getResource("/tile4.png")))
    private val tile5Visual = ImageVisual(ImageIO.read(GameScene::class.java.getResource("/tile5.png")))
    private val tile6Visual = ImageVisual(ImageIO.read(GameScene::class.java.getResource("/tile6.png")))

    private val player2ColorVisual = ImageVisual(ImageIO.read(GameScene::class.java.getResource("/color2.png")))
    private val player3ColorVisual = ImageVisual(ImageIO.read(GameScene::class.java.getResource("/color3.png")))
    private val player4ColorVisual = ImageVisual(ImageIO.read(GameScene::class.java.getResource("/color4.png")))
    private val player5ColorVisual = ImageVisual(ImageIO.read(GameScene::class.java.getResource("/color5.png")))
    private val player6ColorVisual = ImageVisual(ImageIO.read(GameScene::class.java.getResource("/color6.png")))

    init {
        addAll(
            CardView(
                width = 100, height = 100,
                front = tile2Visual
            ),

            CardView(
                posX = 130, posY = 0,
                width = 100, height = 100,
                front = tile3Visual
            ),

            CardView(
                posX = 260, posY = 0,
                width = 100, height = 100,
                front = tile4Visual
            ),

            CardView(
                posX = 390, posY = 0,
                width = 100, height = 100,
                front = tile5Visual
            ),

            CardView(
                posX = 520, posY = 0,
                width = 100, height = 100,
                front = tile6Visual
            ),

            TokenView(
                posX = 0, posY = 122,
                width = 30, height = 6,
                visual = player2ColorVisual
            ),

            Label(
                posX = 47, posY = 116,
                width = 60, height = 12,
                text = "Player 2",
                font = Font(
                    size = 14, color = Color(5, 24, 156),
                    family = DEFAULT_FONT, fontWeight = Font.FontWeight.SEMI_BOLD
                ),
                alignment = Alignment.CENTER_LEFT
            ),

            TokenView(
                posX = 130, posY = 122,
                width = 30, height = 6,
                visual = player3ColorVisual
            ),

            Label(
                posX = 177, posY = 116,
                width = 60, height = 12,
                text = "Player 3",
                font = Font(
                    size = 14, color = Color(5, 24, 156),
                    family = DEFAULT_FONT, fontWeight = Font.FontWeight.SEMI_BOLD
                ),
                alignment = Alignment.CENTER_LEFT
            ),

            TokenView(
                posX = 260, posY = 122,
                width = 30, height = 6,
                visual = player4ColorVisual
            ),

            Label(
                posX = 307, posY = 116,
                width = 60, height = 12,
                text = "Player 4",
                font = Font(
                    size = 14, color = Color(5, 24, 156),
                    family = DEFAULT_FONT, fontWeight = Font.FontWeight.SEMI_BOLD
                ),
                alignment = Alignment.CENTER_LEFT
            ),

            TokenView(
                posX = 390, posY = 122,
                width = 30, height = 6,
                visual = player5ColorVisual
            ),

            Label(
                posX = 437, posY = 116,
                width = 60, height = 12,
                text = "Player 5",
                font = Font(
                    size = 14, color = Color(5, 24, 156),
                    family = DEFAULT_FONT, fontWeight = Font.FontWeight.SEMI_BOLD
                ),
                alignment = Alignment.CENTER_LEFT
            ),

            TokenView(
                posX = 520, posY = 122,
                width = 30, height = 6,
                visual = player6ColorVisual
            ),

            Label(
                posX = 567, posY = 116,
                width = 60, height = 12,
                text = "Player 6",
                font = Font(
                    size = 14, color = Color(5, 24, 156),
                    family = DEFAULT_FONT, fontWeight = Font.FontWeight.SEMI_BOLD
                ),
                alignment = Alignment.CENTER_LEFT
            ),

            Label(
                posX = 47, posY = 137,
                width = 60, height = 12,
                text = "Score: 0",
                font = Font(
                    size = 14, color = Color(5, 24, 156),
                    family = DEFAULT_FONT, fontWeight = Font.FontWeight.SEMI_BOLD
                ),
                alignment = Alignment.CENTER_LEFT
            ),

            Label(
                posX = 177, posY = 137,
                width = 60, height = 12,
                text = "Score: 0",
                font = Font(
                    size = 14, color = Color(5, 24, 156),
                    family = DEFAULT_FONT, fontWeight = Font.FontWeight.SEMI_BOLD
                ),
                alignment = Alignment.CENTER_LEFT
            ),

            Label(
                posX = 307, posY = 137,
                width = 60, height = 12,
                text = "Score: 3",
                font = Font(
                    size = 14, color = Color(5, 24, 156),
                    family = DEFAULT_FONT, fontWeight = Font.FontWeight.SEMI_BOLD
                ),
                alignment = Alignment.CENTER_LEFT
            ),

            Label(
                posX = 437, posY = 137,
                width = 60, height = 12,
                text = "Score: 2",
                font = Font(
                    size = 14, color = Color(5, 24, 156),
                    family = DEFAULT_FONT, fontWeight = Font.FontWeight.SEMI_BOLD
                ),
                alignment = Alignment.CENTER_LEFT
            ),

            Label(
                posX = 567, posY = 137,
                width = 60, height = 12,
                text = "Score: 0",
                font = Font(
                    size = 14, color = Color(5, 24, 156),
                    family = DEFAULT_FONT, fontWeight = Font.FontWeight.SEMI_BOLD
                ),
                alignment = Alignment.CENTER_LEFT
            )
        )
    }
}