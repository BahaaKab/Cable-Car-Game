package view.components

import tools.aqua.bgw.components.ComponentView
import tools.aqua.bgw.components.layoutviews.Pane
import entity.Color
import service.RootService
import tools.aqua.bgw.components.gamecomponentviews.CardView
import tools.aqua.bgw.visual.Visual
import view.*

/**
 * The Pane which contains the other players' tiles, color, and score.
 *
 * Default width: 627, default height: 149.
 *
 * @param posX Horizontal coordinate for this Pane. Default: 0.
 * @param posY Vertical coordinate for this Pane. Default: 0.
 */
class OtherPlayersPane(private val rootService: RootService, posX: Number = 0, posY: Number = 0) :
    Pane<ComponentView>(posX = posX, posY = posY, width = 640, height = 149) {

    private var spacing = 30

    private val otherPlayer1Pane = OtherPlayerPane(posX = 0, posY = 0)

    private val otherPlayer2Pane = OtherPlayerPane(
        posX = 130, posY = 0
    )

    private val otherPlayer3Pane = OtherPlayerPane(
        posX = 260, posY = 0
    )

    private val otherPlayer4Pane = OtherPlayerPane(
        posX = otherPlayer3Pane.posX + otherPlayer3Pane.width + spacing, posY = 0
    )

    private val otherPlayer5Pane = OtherPlayerPane(
        posX = otherPlayer4Pane.posX + otherPlayer4Pane.width + spacing, posY = 0
    )

    init {
        addAll(
            otherPlayer1Pane, otherPlayer2Pane, otherPlayer3Pane, otherPlayer4Pane, otherPlayer5Pane
        )
    }

    fun getUsedPanes() = when(rootService.cableCar.currentState.players.size) {
        2 -> listOf(otherPlayer1Pane)
        3 -> listOf(otherPlayer1Pane, otherPlayer2Pane)
        4 -> listOf(otherPlayer1Pane, otherPlayer2Pane, otherPlayer3Pane)
        5 -> listOf(otherPlayer1Pane, otherPlayer2Pane, otherPlayer3Pane, otherPlayer4Pane)
        else -> listOf(otherPlayer1Pane, otherPlayer2Pane, otherPlayer3Pane, otherPlayer4Pane, otherPlayer5Pane)
    }

    private fun getColorVisual(color: Color) = when (color) {
        Color.YELLOW -> DEFAULT_YELLOW_COLOR
        Color.BLUE -> DEFAULT_BLUE_COLOR
        Color.ORANGE -> DEFAULT_RED_COLOR
        Color.GREEN -> DEFAULT_GREEN_COLOR
        Color.PURPLE -> DEFAULT_PURPLE_COLOR
        Color.BLACK -> DEFAULT_BLUE_COLOR
    }

    fun refreshAfterStartGame() {
        checkNotNull(rootService.cableCar)
        with(rootService.cableCar.currentState) {
            when (players.size) {
                2 -> {
                    otherPlayer1Pane.isVisible = true
                    otherPlayer2Pane.isVisible = false
                    otherPlayer3Pane.isVisible = false
                    otherPlayer4Pane.isVisible = false
                    otherPlayer5Pane.isVisible = false

                    otherPlayer1Pane.posX = width / 2 - otherPlayer1Pane.width / 2
                }

                3 -> {
                    otherPlayer1Pane.isVisible = true
                    otherPlayer2Pane.isVisible = true
                    otherPlayer3Pane.isVisible = false
                    otherPlayer4Pane.isVisible = false
                    otherPlayer5Pane.isVisible = false

                    otherPlayer1Pane.posX = width / 3 - otherPlayer1Pane.width / 2
                    otherPlayer2Pane.posX = 2 * width / 3 - otherPlayer2Pane.width / 2
                }

                4 -> {
                    otherPlayer1Pane.isVisible = true
                    otherPlayer2Pane.isVisible = true
                    otherPlayer3Pane.isVisible = true
                    otherPlayer4Pane.isVisible = false
                    otherPlayer5Pane.isVisible = false

                    otherPlayer1Pane.posX = width / 4 - otherPlayer1Pane.width / 2
                    otherPlayer2Pane.posX = 2 * width / 4 - otherPlayer2Pane.width / 2
                    otherPlayer3Pane.posX = 3 * width / 4 - otherPlayer3Pane.width / 2
                }

                5 -> {
                    otherPlayer1Pane.isVisible = true
                    otherPlayer2Pane.isVisible = true
                    otherPlayer3Pane.isVisible = true
                    otherPlayer4Pane.isVisible = true
                    otherPlayer5Pane.isVisible = false

                    otherPlayer1Pane.posX = width / 5 - otherPlayer1Pane.width / 2
                    otherPlayer2Pane.posX = 2 * width / 5 - otherPlayer2Pane.width / 2
                    otherPlayer3Pane.posX = 3 * width / 5 - otherPlayer3Pane.width / 2
                    otherPlayer4Pane.posX = 4 * width / 5 - otherPlayer4Pane.width / 2
                }

                6 -> {
                    otherPlayer1Pane.isVisible = true
                    otherPlayer2Pane.isVisible = true
                    otherPlayer3Pane.isVisible = true
                    otherPlayer4Pane.isVisible = true
                    otherPlayer5Pane.isVisible = true

                    otherPlayer1Pane.posX = 0.0
                    otherPlayer2Pane.posX = otherPlayer1Pane.posX + 100 + spacing
                    otherPlayer3Pane.posX = otherPlayer2Pane.posX + 100 + spacing
                    otherPlayer4Pane.posX = otherPlayer3Pane.posX + 100 + spacing
                    otherPlayer5Pane.posX = otherPlayer4Pane.posX + 100 + spacing
                }
            }
        }
        refreshOtherPlayers()
    }

    fun refreshOtherPlayers() {
        checkNotNull(rootService.cableCar)
        with(rootService.cableCar.currentState) {
            getUsedPanes().forEachIndexed { index, pane ->
                pane.playerCard.clear()
                if(players[(players.indexOf(activePlayer) + index + 1) % players.size].handTile == null) {
                    pane.playerCard.add(CardView(width = 100, height = 100, front = Visual.EMPTY))
                } else {
                    pane.playerCard.add(tileMapSmall.forward(players[(players.indexOf(activePlayer) + index + 1) % players.size].handTile!!.id).apply { opacity = 1.0 })
                }
                pane.playerNameLabel.text = players[(players.indexOf(activePlayer) + index + 1) % players.size].name
                pane.playerScoreLabel.text = "Score: ${players[(players.indexOf(activePlayer) + index + 1) % players.size].score}"
                pane.playerColorLabel.visual = getColorVisual(players[(players.indexOf(activePlayer) + index + 1) % players.size].color)
            }
        }
    }
}