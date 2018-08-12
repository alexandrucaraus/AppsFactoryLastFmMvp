package eu.caraus.appsflastfm.common.bus

import eu.caraus.appsflastfm.common.bus.events.client.ClientEvent
import eu.caraus.appsflastfm.common.bus.events.service.ServiceEvent

import javax.inject.Singleton

import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject


@Singleton
class RxBus {

    private val service: PublishSubject<in ServiceEvent>
    private val client: PublishSubject<in ClientEvent>

    init {
        service = PublishSubject.create<ServiceEvent>()
        client = PublishSubject.create<ClientEvent>()
    }

    fun sendEventToClient(serviceEvent: ServiceEvent) {
        service.onNext(serviceEvent)
    }

    fun sentEventToService(clientEvent: ClientEvent) {
        client.onNext(clientEvent)
    }

    fun service(): Observable<in ServiceEvent> {
        return service
    }

    fun client(): Observable<in ClientEvent> {
        return client
    }

}
