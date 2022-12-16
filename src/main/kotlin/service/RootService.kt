package service

import entity.CableCar
import view.Refreshable

@Suppress("UNUSED_PARAMETER","UNUSED")

class RootService {
    var cableCar: CableCar? = null
    var playerActionService = PlayerActionService(this)
    var cableCarService = CableCarService(this)
    var setupService = SetupService(this)
    var ioService: IOService? = null
    private val aIService = AIService(this)
    var networkService = NetworkService(this)

    /**
     * Adds the provided [newRefreshable] to all services connected
     * to this root service
     */
    private fun addRefreshable(newRefreshable: Refreshable) {
        playerActionService.addRefreshable(newRefreshable)
        cableCarService.addRefreshable(newRefreshable)
        setupService.addRefreshable(newRefreshable)
        aIService.addRefreshable(newRefreshable) // Turnmessage based? then via regular player options
        networkService.addRefreshable(newRefreshable)
    }

    /**
     * Adds each of the provided [newRefreshables] to all services
     * connected to this root service
     */
    fun addRefreshables(vararg newRefreshables: Refreshable) {
        newRefreshables.forEach { addRefreshable(it) }
    }

}