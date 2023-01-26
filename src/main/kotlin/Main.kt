import tools.aqua.bgw.core.BoardGameApplication
import view.CableCarApplication
import java.io.File
import java.io.FileNotFoundException
import java.nio.file.Files
import java.nio.file.StandardCopyOption

fun main() {
    val stream1 = CableCarApplication::class.java.getResourceAsStream("/johnston-itc-std-bold.ttf")
        ?: throw FileNotFoundException()
    val fontFile1 = File.createTempFile("test1", ".tmp")
    Files.copy(stream1, fontFile1.toPath(), StandardCopyOption.REPLACE_EXISTING)

    val stream2 = CableCarApplication::class.java.getResourceAsStream("/johnston-itc-std-medium.ttf")
        ?: throw FileNotFoundException()
    val fontFile2 = File.createTempFile("test2", ".tmp")
    Files.copy(stream2, fontFile1.toPath(), StandardCopyOption.REPLACE_EXISTING)

    BoardGameApplication.loadFont(fontFile1)
    BoardGameApplication.loadFont(fontFile2)

    CableCarApplication.show()
    println("Application ended. Goodbye")
}