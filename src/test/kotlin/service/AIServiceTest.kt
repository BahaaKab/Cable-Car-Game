package service

import entity.*
import java.lang.Long.max
import kotlin.system.measureTimeMillis
import kotlin.test.*


class AIServiceTest {
    @Test
    fun benchmarkHardAIWithoutRotation() {
        repeat(5) { i ->
            benchmarkHardAI(500, i + 1, false)
        }
    }

    @Test
    fun benchmarkHardAIWithRotation() {
        repeat(5) { i ->
            benchmarkHardAI(500, i + 1, true)
        }
    }


    @Test
    fun benchmarkHardAIPVP() {
        benchmarkHardAI(2000, 1, false)
        benchmarkHardAI(2000, 1, true)
    }


    @Test
    fun runAIClientsInAllPermutations() {
        for(i in 2..6) {
            for (j in 0 until i) {
                val players = List(i) {
                    val playerType = if(it <= j) PlayerType.AI_HARD else PlayerType.AI_EASY
                    PlayerInfo("Player $it", playerType, PLAYER_ORDER_COLORS[it], false)
                }

                val rootService = RootService()
                rootService.setupService.startLocalGame(players, false, 0)

                while (rootService.cableCar.currentState.drawPile.isNotEmpty()) {
                        rootService.aiService.makeAIMove()
                }
            }
        }

    }

    @Suppress("MaxLineLength")
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
                    val elapsedTime = measureTimeMillis { rootService.aiService.makeAIMove() }
                    totalTime += elapsedTime
                    maxTime = max(maxTime, elapsedTime)
                } else {
                    rootService.aiService.makeAIMove()
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
