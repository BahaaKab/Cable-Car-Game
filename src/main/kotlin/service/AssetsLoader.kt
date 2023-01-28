package service

import java.awt.image.BufferedImage
import javax.imageio.ImageIO

object AssetsLoader {
    val rotateLeftImage: BufferedImage = ImageIO.read(AssetsLoader::class.java.getResource("/rotateLeft.png"))
    val rotateRightImage: BufferedImage = ImageIO.read(AssetsLoader::class.java.getResource("/rotateRight.png"))
    val drawTileImage: BufferedImage = ImageIO.read(AssetsLoader::class.java.getResource("/drawTile.png"))
    val backArrowImage: BufferedImage = ImageIO.read(AssetsLoader::class.java.getResource("/arrow.png"))
    val refreshArrowGreyImage: BufferedImage = ImageIO.read(AssetsLoader::class.java.getResource("/arrow_refresh.png"))
    val refreshArrowBlueImage: BufferedImage = ImageIO.read(AssetsLoader::class.java.getResource("/arrow_refresh_blue.png"))
    val cubeImage: BufferedImage = ImageIO.read(AssetsLoader::class.java.getResource("/cube.png"))
    val undoImage: BufferedImage = ImageIO.read(AssetsLoader::class.java.getResource("/undo.png"))
    val redoImage: BufferedImage = ImageIO.read(AssetsLoader::class.java.getResource("/redo.png"))
    val boardImage: BufferedImage = ImageIO.read(AssetsLoader::class.java.getResource("/board.png"))
}