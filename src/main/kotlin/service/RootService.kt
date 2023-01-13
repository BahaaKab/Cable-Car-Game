package service

import entity.CableCar
import view.Refreshable

/**
 * The [RootService] is used to connect every Class in the project
 * **/
class RootService {
    lateinit var cableCar: CableCar
    val playerActionService = PlayerActionService(this)
    val cableCarService = CableCarService(this)
    val setupService = SetupService(this)
    val ioService = IOService(this)
    val aIService = AIService(this)
    val networkService = NetworkService(this)

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