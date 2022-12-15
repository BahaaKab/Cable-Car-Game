package service

import entity.CableCar

class RootService {
    var cableCar: CableCar? = null
    var playerActionService: PlayerActionService? = null
    var cableCarService: CableCarService? = null
    var setupService: SetupService? = null
    var ioService: IOService? = null
    private val aIService: AIService? = null
    var aiService: AIService? = null
    var networkService: NetworkService? = null
}