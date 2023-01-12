package view.components

import tools.aqua.bgw.components.layoutviews.Pane
import tools.aqua.bgw.components.uicomponents.Label
import tools.aqua.bgw.core.Alignment
import tools.aqua.bgw.util.Font
import view.DEFAULT_BLUE
import view.DEFAULT_BLUE_VISUAL
import view.DEFAULT_FONT_BOLD
import java.awt.Color

/**
 * The CableCar Underground Logo.
 *
 * Default width: 238, default height: 89.
 *
 * @param posX Horizontal coordinate for this Pane. Default: 0.
 * @param posY Vertical coordinate for this Pane. Default: 0.
 */
class CableCarLogo(posX: Number = 0, posY: Number = 0) :
    Pane<Label>(posX = posX, posY = posY, width = 238, height = 89) {

    private val cableCarLabel = Label(
        posX = 0, posY = 0,
        width = 238, height = 64,
        text = "CABLECAR",
        font = Font(size = 40, color = Color.WHITE, family = DEFAULT_FONT_BOLD),
        alignment = Alignment.CENTER,
        visual = DEFAULT_BLUE_VISUAL
    ).apply {
        backgroundStyle = "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.16), 3,0,0,3);"
    }

    private val undergroundLabel = Label(
        posX = 0, posY= 65,
        width = 238, height = 24,
        text = "UNDERGROUND",
        font = Font(size = 27, color = DEFAULT_BLUE, family = DEFAULT_FONT_BOLD),
        alignment = Alignment.CENTER
    )

    init {
        addAll(cableCarLabel, undergroundLabel)
    }
}