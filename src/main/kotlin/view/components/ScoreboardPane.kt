package view.components

import service.RootService
import tools.aqua.bgw.components.layoutviews.Pane
import tools.aqua.bgw.components.uicomponents.Label
import tools.aqua.bgw.components.uicomponents.LabeledUIComponent
import tools.aqua.bgw.core.Alignment
import tools.aqua.bgw.util.Font
import view.DEFAULT_BLUE
import view.DEFAULT_FONT_MEDIUM

class ScoreboardPane(private val rootService: RootService, posX: Number = 0, posY: Number = 0) :
    Pane<LabeledUIComponent>(posX, posY, 0, 0) {

    private val defaultFont = Font(
        size = 25,
        color = DEFAULT_BLUE,
        family = DEFAULT_FONT_MEDIUM
    )

    private val defaultWidth = 300
    private val defaultHeight = 50

    private val defaultScoreWidth = 50

    private val playerNameLabels = List(6) { i ->
        Label(
            posX = 0,
            posY = 60 * i,
            width = defaultWidth,
            height = defaultHeight,
            font = defaultFont,
            alignment = Alignment.CENTER_LEFT
        )
    }

    private val playerScoreLabels = List(6) { i ->
        Label(
            posX = defaultWidth + 20, posY = 60 * i,
            width = defaultScoreWidth, height = defaultHeight,
            font = defaultFont,
            alignment = Alignment.CENTER_RIGHT
        )
    }

    init {
        addAll(playerNameLabels)
        addAll(playerScoreLabels)
    }

    fun getUsedNameLabels() = playerNameLabels.subList(0, rootService.cableCar.currentState.players.size)

    fun getUsedPointsLabels() = playerScoreLabels.subList(0, rootService.cableCar.currentState.players.size)


    fun showOnlyRelevant() {
        checkNotNull(rootService.cableCar)
        playerNameLabels.zip(playerScoreLabels) { name, score ->
            name.isVisible = false
            score.isVisible = false
        }

        playerNameLabels.subList(
            0, rootService.cableCar.currentState.players.size
        ).zip(playerScoreLabels) { name, score ->
            name.isVisible = true
            score.isVisible = true
        }

    }
}