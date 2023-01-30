package service

import tools.aqua.bgw.net.common.notification.Notification
import tools.aqua.bgw.net.common.notification.PlayerJoinedNotification
import tools.aqua.bgw.net.common.notification.PlayerLeftNotification
import tools.aqua.bgw.net.common.response.*
import view.Refreshable
import java.util.concurrent.TimeoutException

/**
 * A class to handle network [Response] and [Notification]
 */
class NetworkRefreshable : Refreshable {
    /**
     * A response or null. May be either a [CreateGameResponse] or a [JoinGameResponse]
     */
    var response: Response? = null

    /**
     * A notification or null. May be either a [PlayerJoinedNotification] or a [PlayerLeftNotification]
     */
    var notification: Notification? = null

    /**
     *
     */
    var hasStartedGameInstance = false

    /**
     *
     */
    var hasReceivedTurnMessage = false

    /**
     * Set the [NetworkRefreshable.response] property
     *
     * @param response The response
     */
    override fun refreshAfterNetworkResponse(response: Response) {
        this.response = response
    }

    /**
     * Set the [NetworkRefreshable.notification] property
     *
     * @param notification The notification
     */
    override fun refreshAfterNetworkNotification(notification: Notification) {
        this.notification = notification
    }

    /**
     *
     */
    override fun refreshAfterStartGame() {
        hasStartedGameInstance = true
    }

    /**
     *
     */
    fun awaitGameInitMessageWithin(timeoutInMillis: Int, block: () -> Unit) {
        hasStartedGameInstance = false

        block()

        var counter = timeoutInMillis
        while (!hasStartedGameInstance) {
            Thread.sleep(5)
            if (counter <= 0) {
                throw TimeoutException()
            }
            counter -= 5
        }
    }

    fun awaitTurnMessageWithin(timeoutInMillis: Int, block: () -> Unit) {
        hasReceivedTurnMessage = false

        block()

        var counter = timeoutInMillis
        while (!hasReceivedTurnMessage) {
            Thread.sleep(5)
            if (counter <= 0) {
                throw TimeoutException()
            }
            counter -= 5
        }
    }

    /**
     * Whether a connection was successful within a timeout duration.
     * A connection is successful when the response was returning either a [CreateGameResponseStatus.SUCCESS] or a
     * [JoinGameResponseStatus.SUCCESS].
     *
     * @param timeoutInMillis The timeout duration in milliseconds
     *
     * @return Whether the connection was successful
     */
    fun responseSuccessWithin(timeoutInMillis: Int): Boolean {
        responseWithinOrNull(timeoutInMillis)
        return (response is JoinGameResponse && (response as JoinGameResponse).status == JoinGameResponseStatus.SUCCESS) ||
                (response is CreateGameResponse && (response as CreateGameResponse).status == CreateGameResponseStatus.SUCCESS)
    }

    /**
     * Return a notification with in a timeout duration or null otherwise.
     *
     * @param timeoutInMillis The timeout duration in milliseconds
     *
     * @return A notification or null, when the timeout was exceeded
     */
    fun notificationWithinOrNull(timeoutInMillis: Int): Notification? {
        var counter = timeoutInMillis
        while (notification == null) {
            Thread.sleep(5)
            if (counter <= 0) {
                return null
            }
            counter -= 5
        }
        return notification
    }

    /**
     * Return a response with in a timeout duration or null otherwise.
     *
     * @param timeoutInMillis The timeout duration in milliseconds
     *
     * @return A response or null, when the timeout was exceeded
     */
    private fun responseWithinOrNull(timeoutInMillis: Int): Response? {
        var counter = timeoutInMillis
        while (response == null) {
            Thread.sleep(5)
            if (counter <= 0) {
                return null
            }
            counter -= 5
        }
        return response
    }
}