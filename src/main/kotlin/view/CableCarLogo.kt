package view

import tools.aqua.bgw.components.layoutviews.Pane
import tools.aqua.bgw.components.uicomponents.Label
import tools.aqua.bgw.core.Alignment
import tools.aqua.bgw.util.Font
import tools.aqua.bgw.visual.ColorVisual
import java.awt.Color

class CableCarLogo(posX: Number = 0, posY: Number = 0) :
    Pane<Label>(posX = posX, posY = posY, width = 0, height = 0) {

        init {
            addAll(
                Label(
                    posX = 0, posY = 0,
                    width = 238, height = 64,
                    text = "CABLECAR",
                    font = Font(size = 40, color = Color.WHITE,
                        family = DEFAULT_FONT, fontWeight = Font.FontWeight.BOLD),
                    alignment = Alignment.CENTER,
                    visual = ColorVisual(5, 24, 156)
                    ).apply {
                        backgroundStyle = "-fx-effect: dropshadow( gaussian , rgba(0,0,0,0.16) , 3,0,0,3 );"
                    },

                Label(
                    posX = 0, posY= 60,
                    width = 238, height = 24,
                    text = "UNDERGROUND",
                    font = Font(size = 27, color = Color(5, 24, 156),
                        family = DEFAULT_FONT, fontWeight = Font.FontWeight.BOLD),
                    alignment = Alignment.CENTER
                )
            )
        }
}