package service

import java.awt.image.BufferedImage
import javax.imageio.ImageIO
import kotlin.test.*


/**
 * Tests equality of two [BufferedImage]s by first checking if they have the same dimensions
 * and then comparing every pixels' RGB value.
 */
private infix fun BufferedImage.sameAs(other: Any?): Boolean {

    // if the other is not even a BufferedImage, we are done already
    if (other !is BufferedImage) {
        return false
    }

    // check dimensions
    if (this.width != other.width || this.height != other.height) {
        return false
    }

    // compare every pixel
    for (y in 0 until height) {
        for (x in 0 until width) {
            if (this.getRGB(x, y) != other.getRGB(x, y))
                return false
        }
    }

    // if we reach this point, dimensions and pixels match
    return true

}
