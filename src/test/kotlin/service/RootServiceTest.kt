package service

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

/**
 * This class is used to test the Service class [RootService]
 * **/
class RootServiceTest {

    /**
     * Creates a new [RootService] object and tests it
     * **/
    @Test
    fun testConstructor() {
        val rootService = RootService()
        val playerActionService = PlayerActionService(rootService)
        val cableCarService = CableCarService(rootService)
        val setupService = SetupService(rootService)
        val ioService = IOService(rootService)
        val aiService = AIService(rootService)
        val networkService = NetworkService(rootService)

        assertEquals(rootService, rootService)
        assertEquals(playerActionService, rootService.playerActionService)
        assertEquals(cableCarService, rootService.cableCarService)
        assertEquals(setupService, rootService.setupService)
        assertEquals(ioService, rootService.ioService)
        assertEquals(aiService, rootService.aIService)
        assertEquals(networkService, rootService.networkService)
        assertNotNull(rootService)
    }

}