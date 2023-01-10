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

        assertEquals(rootService, rootService)
        assertNotNull(rootService)
    }

}