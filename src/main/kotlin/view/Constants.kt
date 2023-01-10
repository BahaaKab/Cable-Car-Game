package view

import service.TileImageLoader
import tools.aqua.bgw.visual.ColorVisual
import java.awt.Color

const val DEFAULT_FONT_BOLD = "Johnston ITC Std Light" // It says light but is in fact bold
const val DEFAULT_FONT_MEDIUM = "Johnston ITC Std Medium"
val DEFAULT_BLUE = Color(5, 24, 156)
val DEFAULT_BLUE_VISUAL = ColorVisual(5, 24, 156)
const val DEFAULT_GREY_STRING = "233,233,236"
val DEFAULT_YELLOW_COLOR = ColorVisual(253,211,41)
val DEFAULT_BLUE_COLOR = ColorVisual(17,139,206)
val DEFAULT_RED_COLOR = ColorVisual(213,41,39)
val DEFAULT_GREEN_COLOR = ColorVisual(12,111,47)
val DEFAULT_PURPLE_COLOR = ColorVisual(142,13,78)
const val DEFAULT_BORDER_RADIUS = "20px"
const val DEFAULT_SHADOW =
    "-fx-effect: dropshadow( gaussian , rgba(0,0,0,0.2) , 10,0,2,4 );"
val TILEIMAGELOADER = TileImageLoader()