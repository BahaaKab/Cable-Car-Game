package service

import entity.Color
import java.awt.image.BufferedImage
import javax.imageio.ImageIO

private const val CARDS_FILE = "/tiles.jpg"
private const val IMG_WIDTH = 100
private const val IMG_HEIGHT = 100


@Suppress("UNUSED")

/**
 * Provides access to the src/main/resources/tiles.jpg file that contains all tile images
 * in a raster. The returned [BufferedImage] objects of [frontImageFor] are 100x100 pixels.
 */
class TileImageLoader {

    /**
     * The full raster image containing the tile.
     */
    private val image: BufferedImage = ImageIO.read(TileImageLoader::class.java.getResource(CARDS_FILE))

    /**
     * Provides the tile image for the given [entity.GameTile.id]
     */
    fun frontImageFor(id: Int) = getImageByCoordinates(id % 10, id / 10)

    /**
     * Provides the station tile image for the given [entity.Color]
     */
    fun stationTileFor(color: Color) = when (color) {
        Color.YELLOW -> getImageByCoordinates(0, 6)
        Color.BLUE -> getImageByCoordinates(1, 6)
        Color.ORANGE -> getImageByCoordinates(2, 6)
        Color.GREEN -> getImageByCoordinates(3, 6)
        Color.PURPLE -> getImageByCoordinates(4, 6)
        Color.BLACK -> getImageByCoordinates(5, 6)
    }


    /**
     * Retrieves from the full raster image [image] the corresponding sub-image
     * for the given column [x] and row [y]
     *
     * @param x column in the raster image, starting at 0
     * @param y row in the raster image, starting at 0
     *
     */
    private fun getImageByCoordinates(x: Int, y: Int): BufferedImage =
        image.getSubimage(
            x * IMG_WIDTH,
            y * IMG_HEIGHT,
            IMG_WIDTH,
            IMG_HEIGHT
        )

}