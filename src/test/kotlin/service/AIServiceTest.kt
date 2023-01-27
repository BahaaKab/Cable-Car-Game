package service

import entity.*
import java.lang.Long.max
import kotlin.system.measureTimeMillis
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
        val enemies = List(numberOfEasyEnemies) { i ->
            PlayerInfo("Easy AI$i", PlayerType.AI_EASY, PLAYER_ORDER_COLORS[i], false)
        }
        val playerInfos = listOf(hardAI) + enemies

        var hardAIWins = 0f
        var totalActions = 0
        var totalTime = 0L
        var maxTime = 0L
        repeat(numberOfRuns) {
            val rootService = RootService()
            rootService.setupService.startLocalGame(playerInfos.shuffled(), false, 0)

            while (rootService.cableCar.currentState.drawPile.isNotEmpty()) {
                if (rootService.cableCar.currentState.activePlayer.name == hardAI.name) {
                    totalActions ++
                    val elapsedTime = measureTimeMillis { rootService.aIService.makeAIMove() }
                    totalTime += elapsedTime
                    maxTime = max(maxTime, elapsedTime)
                } else {
                    rootService.aIService.makeAIMove()
                }
            }

            if (rootService.cableCar.currentState.players.sortedByDescending { it.score }.first().name == hardAI.name) {
                hardAIWins ++
            }
        }

        val winRate = hardAIWins / numberOfRuns
        val average = 1f / playerInfos.size
        val diff = winRate - average
        println("Win rate after $numberOfRuns games against $numberOfEasyEnemies enemies: ${hardAIWins / numberOfRuns}%. Diff to average: $diff.")
        println("Average elapsed Time: ${totalTime / totalActions} ms. Max time elapsed for a single action: ${maxTime} ms.")
    }


}
