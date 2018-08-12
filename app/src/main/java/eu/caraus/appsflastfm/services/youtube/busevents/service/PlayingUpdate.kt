package eu.caraus.appsflastfm.services.youtube.busevents.service

import eu.caraus.appsflastfm.common.bus.events.service.ServiceEvent
import eu.caraus.appsflastfm.services.youtube.model.youtube.YouTubeVideo

class PlayingUpdate(val youTubeVideo: YouTubeVideo) : ServiceEvent()