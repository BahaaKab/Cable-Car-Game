package service

import entity.*
import kotlin.test.*

class AIServiceTest {
    @Test
    fun benchmarkHardAIWithoutRotation() {
        repeat(5) { i ->
            benchmarkHardAIWithoutRotation(1000, i + 1)
        }

    }

    fun benchmarkHardAIWithoutRotation(numberOfRuns: Int, numberOfEasyEnemies: Int) {
        require(numberOfEasyEnemies in 1..5)

        val hardAI = PlayerInfo("Hard AI", PlayerType.AI_HARD, Color.YELLOW, false)
        var hardAIWins = 0f

        val enemies = List(numberOfEasyEnemies) { i ->
            PlayerInfo("Easy AI$i", PlayerType.AI_EASY, PLAYER_ORDER_COLORS[i], false)
        }

        val playerInfos = listOf(hardAI) + enemies

        repeat(numberOfRuns) {
            val rootService = RootService()
            rootService.setupService.startLocalGame(playerInfos.shuffled(), false, 0)

            while (rootService.cableCar.currentState.drawPile.isNotEmpty()) {
                rootService.aIService.makeAIMove()
            }

            if (rootService.cableCar.currentState.players.sortedByDescending { it.score }.first().name == hardAI.name) {
                hardAIWins ++
            }
        }

        val winRate = hardAIWins / numberOfRuns
        val average = 1f / playerInfos.size
        val diff = winRate - average
        println("Win rate after $numberOfRuns games against $numberOfEasyEnemies enemies: ${hardAIWins / numberOfRuns}%. Diff to average: $diff")
    }


}
