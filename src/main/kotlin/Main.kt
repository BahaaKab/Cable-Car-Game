import tools.aqua.bgw.core.BoardGameApplication
import view.CableCarApplication
import java.io.File
import java.io.FileNotFoundException

fun main() {
    val uri1 = CableCarApplication::class.java.getResource("/johnston-itc-std-bold.ttf")?.toURI()
        ?: throw FileNotFoundException()
    val fontFile1 = File(uri1)

    val uri2 = CableCarApplication::class.java.getResource("/johnston-itc-std-medium.ttf")?.toURI()
        ?: throw FileNotFoundException()
    val fontFile2 = File(uri2)

    BoardGameApplication.loadFont(fontFile1)
    BoardGameApplication.loadFont(fontFile2)

    CableCarApplication().show()
    println("Application ended. Goodbye")
}