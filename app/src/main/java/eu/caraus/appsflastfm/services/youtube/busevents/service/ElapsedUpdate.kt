package eu.caraus.appsflastfm.services.youtube.busevents.service

import eu.caraus.appsflastfm.common.bus.events.service.ServiceEvent
import eu.caraus.appsflastfm.services.youtube.model.youtube.YouTubeVideo

class ElapsedUpdate ( val youTubeVideo: YouTubeVideo, val elapsed : Int ) : ServiceEvent()