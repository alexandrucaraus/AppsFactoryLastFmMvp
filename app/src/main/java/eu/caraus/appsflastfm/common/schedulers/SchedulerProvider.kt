package eu.caraus.appsflastfm.common.schedulers

import io.reactivex.Scheduler

/**
 *  Abstraction over Reactive Scheduler, gives ability
 *  to use different schedulers in real and test environment
 */

interface SchedulerProvider {

    fun ui(): Scheduler
    fun io(): Scheduler
    fun computation(): Scheduler

}
