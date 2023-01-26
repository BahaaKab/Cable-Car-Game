package view

import tools.aqua.bgw.core.MenuScene
import service.RootService
import tools.aqua.bgw.components.uicomponents.Button
import tools.aqua.bgw.core.Alignment
import tools.aqua.bgw.util.Font
import tools.aqua.bgw.visual.ColorVisual
import tools.aqua.bgw.visual.Visual
import view.components.CableCarLogo
import view.components.ScoreboardPane
import java.awt.Color

/**
 * This class manages the end scene of the application. Here the players see their final scores and have
 * the option to exit the game or go back to the main menu.
 *
 * @param rootService The administration class for the entity and service layer.
 */
class EndScene(private val rootService: RootService) : MenuScene(1920, 1080), Refreshable {

    private val logoPane = CableCarLogo(posX = 817, posY = 104).apply {
        scale = 1.2
    }

    private val scoreboardPane = ScoreboardPane(rootService = rootService, posX = 775, posY = 300)

    val exitButton = Button(
        posX = 870, posY = 775,
        width = 180, height = 50,
        text = "Exit",
        font = Font(size = 25, color = Color.WHITE, family = DEFAULT_FONT_MEDIUM),
        alignment = Alignment.CENTER,
        visual = Visual.EMPTY
    ).apply {
        componentStyle = "-fx-background-color: rgb($DEFAULT_BLUE_STRING);-fx-background-radius: 30"
    }

    init {
        opacity = 1.0
        background = ColorVisual(247, 247, 247)
        addComponents(
            logoPane, scoreboardPane, exitButton
        )
    }

    override fun refreshAfterEndGame(closeApplication : Boolean) {
        scoreboardPane.showOnlyRelevant()
        CableCarApplication.showMenuScene(this)

        val playersSorted = rootService.cableCar.currentState.players.sortedByDescending {
            it.score
        }

        val pointsList = playersSorted.map { it.score }

        val rankingMap = mutableMapOf<Int, Int>().apply {

            // toSet() removes duplicates, and then we need a list to iterate over
            pointsList.toSet().toList().forEachIndexed { index, int ->
                put(int, index + 1)
            }
        }

        scoreboardPane.getUsedNameLabels().forEachIndexed { index, label ->
            label.text = "${rankingMap[pointsList[index]]}. ${playersSorted[index].name}"
        }

        scoreboardPane.getUsedPointsLabels().forEachIndexed { index, label ->
            label.text = "${pointsList[index]}"
        }
    }
}