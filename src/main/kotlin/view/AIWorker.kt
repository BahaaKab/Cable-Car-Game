package view

import service.RootService
import tools.aqua.bgw.core.BoardGameApplication

class AIWorker(private val rootService: RootService): Thread() {
    var isAIPlayer = false
    override fun run() {
        while (!rootService.gameEnded) {
            Thread.sleep(100)
            if (isAIPlayer) {
                BoardGameApplication.runOnGUIThread {
                    rootService.aIService.makeAIMove()
                }
                Thread.sleep(300)
            }
            Thread.sleep(100)
        }
    }
}