package eu.caraus.appsflastfm.services.youtube.busevents.service

import eu.caraus.appsflastfm.common.bus.events.service.ServiceEvent

class ErrorUpdate( var message : String ) : ServiceEvent()