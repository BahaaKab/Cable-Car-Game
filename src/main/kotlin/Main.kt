import tools.aqua.bgw.core.BoardGameApplication
import view.CableCarApplication
import java.io.File
import java.io.FileNotFoundException

fun main() {
    val uri = CableCarApplication::class.java.getResource("/Johnston-ITC-Std-Medium.ttf")?.toURI()
        ?: throw FileNotFoundException()
    BoardGameApplication.loadFont(File(uri))

    val uri2 = CableCarApplication::class.java.getResource("/johnston-itc-std-bold.ttf")?.toURI()
        ?: throw FileNotFoundException()
    BoardGameApplication.loadFont(File(uri2))

    CableCarApplication().show()
    println("Application ended. Goodbye")
}