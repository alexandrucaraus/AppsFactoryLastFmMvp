package eu.caraus.appsflastfm.services.youtube.busevents.service

import eu.caraus.appsflastfm.common.bus.events.service.ServiceEvent
import eu.caraus.appsflastfm.services.youtube.model.youtube.YouTubeVideo

class StoppedUpdate(val youTubeVideo: YouTubeVideo) : ServiceEvent()