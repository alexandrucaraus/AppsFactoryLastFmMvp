package eu.caraus.appsflastfm.common.schedulers

import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers

/**
 *  Implementation of [SchedulerProvider] to be used in testing
 */

class TestSchedulerProvider : SchedulerProvider {

    override fun ui(): Scheduler {
        return Schedulers.trampoline()
    }

    override fun io(): Scheduler {
        return Schedulers.trampoline()
    }

    override fun computation(): Scheduler {
        return Schedulers.trampoline()
    }

}
