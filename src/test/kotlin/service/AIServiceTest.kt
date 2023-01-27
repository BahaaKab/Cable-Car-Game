package service

import entity.*
import java.lang.Long.max
import kotlin.system.measureTimeMillis
import kotlin.test.*


class AIServiceTest {
    @Test
    fun benchmarkHardAIWithoutRotation() {
        repeat(5) { i ->
            benchmarkHardAI(1000, i + 1, false)
        }
    }

    @Test
    fun benchmarkHardAIWithRotation() {
        repeat(5) { i ->
            benchmarkHardAI(1000, i + 1, true)
        }
    }


    @Test
    fun benchmarkHardAIPVP() {
        benchmarkHardAI(1000, 1, false)
        benchmarkHardAI(1000, 1, true)
    }

    private fun benchmarkHardAI(numberOfRuns: Int, numberOfEasyEnemies: Int, allowRotation: Boolean) {
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
            rootService.setupService.startLocalGame(playerInfos.shuffled(), allowRotation, 0)

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

            if (rootService.cableCar.currentState.players.maxByOrNull { it.score }?.name == hardAI.name) {
                hardAIWins ++
            }
        }

        val winRate = hardAIWins / numberOfRuns
        val average = 1f / playerInfos.size
        val diff = winRate - average
        println("==================== AI benchmark test ${ if (allowRotation) "with rotation" else "without rotation"} ===================")
        println("Win rate after $numberOfRuns games against $numberOfEasyEnemies enemies: ${hardAIWins / numberOfRuns}%. Diff to average: $diff.")
        println("Average elapsed Time: ${totalTime / totalActions} ms. Max time elapsed for a single action: $maxTime ms.")
        println("========================================================================")
    }


}
