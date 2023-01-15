package view.components

import tools.aqua.bgw.components.layoutviews.Pane
import tools.aqua.bgw.components.uicomponents.Label
import tools.aqua.bgw.components.uicomponents.LabeledUIComponent
import tools.aqua.bgw.core.Alignment
import tools.aqua.bgw.util.Font
import view.DEFAULT_BLUE
import view.DEFAULT_FONT_MEDIUM

class ScoreboardPane(posX: Number = 0, posY: Number = 0) :
    Pane<LabeledUIComponent>(posX, posY, 0, 0) {

    private val defaultFont = Font(
        size = 25,
        color = DEFAULT_BLUE,
        family = DEFAULT_FONT_MEDIUM
    )

    private val defaultWidth = 300
    private val defaultHeight = 50

    private val defaultScoreWidth = 50

    private val p1Label = Label(
        posX = 0, posY = 0,
        width = defaultWidth, height = defaultHeight,
        text = "1. Player 5",
        font = defaultFont,
        alignment = Alignment.CENTER_LEFT
    )

    private val p2Label = Label(
        posX = 0, posY = 60,
        width = defaultWidth, height = defaultHeight,
        text = "2. Player 2",
        font = defaultFont,
        alignment = Alignment.CENTER_LEFT
    )

    private val p3Label = Label(
        posX = 0, posY = 120,
        width = defaultWidth, height = defaultHeight,
        text = "3. Player 3",
        font = defaultFont,
        alignment = Alignment.CENTER_LEFT
    )

    private val p4Label = Label(
        posX = 0, posY = 180,
        width = defaultWidth, height = defaultHeight,
        text = "4. Player 1",
        font = defaultFont,
        alignment = Alignment.CENTER_LEFT
    )

    private val p5Label = Label(
        posX = 0, posY = 240,
        width = defaultWidth, height = defaultHeight,
        text = "5. Player 4",
        font = defaultFont,
        alignment = Alignment.CENTER_LEFT
    )

    private val p6Label = Label(
        posX = 0, posY = 300,
        width = defaultWidth, height = defaultHeight,
        text = "6. Player 6",
        font = defaultFont,
        alignment = Alignment.CENTER_LEFT
    )

    private val p1PointsLabel = Label(
        posX = defaultWidth + 20, posY = 0,
        width = defaultScoreWidth, height = defaultHeight,
        text = "39",
        font = defaultFont,
        alignment = Alignment.CENTER_RIGHT
    )

    private val p2PointsLabel = Label(
        posX = defaultWidth + 20, posY = 60,
        width = defaultScoreWidth, height = defaultHeight,
        text = "26",
        font = defaultFont,
        alignment = Alignment.CENTER_RIGHT
    )

    private val p3PointsLabel = Label(
        posX = defaultWidth + 20, posY = 120,
        width = defaultScoreWidth, height = defaultHeight,
        text = "21",
        font = defaultFont,
        alignment = Alignment.CENTER_RIGHT
    )

    private val p4PointsLabel = Label(
        posX = defaultWidth + 20, posY = 180,
        width = defaultScoreWidth, height = defaultHeight,
        text = "15",
        font = defaultFont,
        alignment = Alignment.CENTER_RIGHT
    )

    private val p5PointsLabel = Label(
        posX = defaultWidth + 20, posY = 240,
        width = defaultScoreWidth, height = defaultHeight,
        text = "14",
        font = defaultFont,
        alignment = Alignment.CENTER_RIGHT
    )

    private val p6PointsLabel = Label(
        posX = defaultWidth + 20, posY = 300,
        width = defaultScoreWidth, height = defaultHeight,
        text = "5",
        font = defaultFont,
        alignment = Alignment.CENTER_RIGHT
    )

    init {
        addAll(
            p1Label,
            p2Label,
            p3Label,
            p4Label,
            p5Label,
            p6Label,
            p1PointsLabel,
            p2PointsLabel,
            p3PointsLabel,
            p4PointsLabel,
            p5PointsLabel,
            p6PointsLabel,
        )
    }
}