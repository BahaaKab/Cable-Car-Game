package service

import tools.aqua.bgw.net.common.response.CreateGameResponseStatus
import kotlin.random.Random
import kotlin.random.nextUInt
import kotlin.test.BeforeTest

open class NetworkTest {
    /**
     * A timeout default. Waiting for more than 5 seconds for a response or notification should be considered as
     * a fail, which is most likely caused by the server.
     */
    protected val timeoutInMillis = 5000

    /**
     * The base session ID. For more infos see [setUniqueSessionId].
     */
    protected val coreSessionID = "CableCarSession"

    /**
     * A random starting ID. For more infos see [setUniqueSessionId].
     */
    private var sessionIDCounter = Random.nextUInt()

    /**
     * The actual session ID. For more infos see [setUniqueSessionId].
     */
    protected var sessionID = ""

    /**
     * Generate a unique session ID for each test.
     * As the server keeps a session alive for a few seconds after the last player left it, the tests might run in
     * unexpected errors, when they use the same session ID, as the server might return a
     * [CreateGameResponseStatus.SESSION_WITH_ID_ALREADY_EXISTS] status. Therefore this method generates unique session
     * IDs for each test. The [sessionIDCounter] is initialized randomly, when the class is created. This method will
     * be called automatically before each test and will set the [sessionID] with a incremented [sessionIDCounter].
     */
    private fun setUniqueSessionId() {
        sessionID = "${coreSessionID}_${sessionIDCounter ++}"
    }

    @BeforeTest
    open fun setup() {
        setUniqueSessionId()
    }
}