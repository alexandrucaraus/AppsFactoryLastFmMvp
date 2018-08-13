package eu.caraus.appsflastfm.services.youtube.busevents.client

import eu.caraus.appsflastfm.common.bus.events.client.ClientEvent
import eu.caraus.appsflastfm.services.youtube.model.youtube.YouTubeVideo

class ActionSeek( var seekTo : Int ) : ClientEvent()