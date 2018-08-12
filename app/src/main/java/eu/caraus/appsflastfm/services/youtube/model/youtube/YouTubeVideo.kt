package eu.caraus.appsflastfm.services.youtube.model.youtube

import eu.caraus.appsflastfm.data.domain.extensions.lastFm.TrackState
import java.io.Serializable

data class YouTubeVideo (

    var id: String? = "",
    var title: String? = "",
    var thumbnailURL: String? = "",
    var duration: String? = "",
    var viewCount : String? = "",
    var trackId : Long = 0,
    var trackState : TrackState = TrackState.STOPPED,
    var trackElapsed : Int = 0

    ) : Serializable
