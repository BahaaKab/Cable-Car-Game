package service

import entity.*
import org.junit.jupiter.api.Test
import tools.aqua.bgw.visual.CompoundVisual
import tools.aqua.bgw.visual.ImageVisual
import java.awt.*;
import java.awt.Color
import java.awt.image.*;
import javax.swing.*;
class TileImageLoaderTest {
    @Test
    fun test() {
        val rootService = RootService()
        val p1 = Player(PlayerType.HUMAN, entity.Color.BLUE, "Player_1", listOf(), false)
        val p2 = Player(PlayerType.HUMAN, entity.Color.GREEN, "Player_2", listOf(), false)
        val p3 = Player(PlayerType.HUMAN, entity.Color.BLACK, "Player_3", listOf(), false)
        val testBoard : Array<Array<Tile?>> = Array(10) {
            Array(10) { null }
        }
        val state = State(rootService.ioService.getTilesFromCSV().toMutableList(), p1, testBoard, listOf(p1,p2,p3))
        rootService.cableCar = CableCar(false, 10, false, GameMode.HOTSEAT, History(), state)

        val imgDim = Dimension(1000, 1000)
        val mazeImage = BufferedImage(imgDim.width, imgDim.height, BufferedImage.TYPE_INT_ARGB)


        val g2d = mazeImage.createGraphics()
        g2d.background = Color(0,0,0,0)
        g2d.fillRect(0, 0, imgDim.width, imgDim.height)
        g2d.color = Color.BLACK
        val bs = BasicStroke(2.0f)
        g2d.stroke = bs
        // draw the black vertical and horizontal lines
        // draw the black vertical and horizontal lines
        for (i in 0..20) {
            // unless divided by some factor, these lines were being
            // drawn outside the bound of the image
            g2d.drawLine((imgDim.width + 2) / 20 * i, 0, (imgDim.width + 2) / 20 * i, imgDim.height - 1)
            g2d.drawLine(0, (imgDim.height + 2) / 20 * i, imgDim.width - 1, (imgDim.height + 2) / 20 * i)
        }

        val visual = ImageVisual(mazeImage)

        val ii = ImageIcon(mazeImage)
        //Comment in for showing GUI and debugging, but comment out for CI
        //JOptionPane.showMessageDialog(null, ii)
    }
}