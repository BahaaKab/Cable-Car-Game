package service

import view.Refreshable

/**
 *  Abstract Service class representing the subject of an observer pattern.
 *
 *  This class should be implemented by all services that need to notify the GUI to refresh.
 *  @property refreshables All registered refreshables
 */

abstract class AbstractRefreshingService {
    private val refreshables = mutableListOf<Refreshable>()

    /**
     * Register a new [Refreshable].
     *
     * @param newRefreshable An unregistered [Refreshable].
     */
    fun addRefreshable(newRefreshable: Refreshable) {
        if (newRefreshable !in refreshables) { refreshables.add(newRefreshable) }
    }

    /**
     * Notify all registered refreshables.
     *
     * @param method The method that should be called by the refreshables.
     */
    fun onAllRefreshables(method: Refreshable.() -> Unit) {
        for (refreshable in refreshables) { refreshable.method() }
    }

}