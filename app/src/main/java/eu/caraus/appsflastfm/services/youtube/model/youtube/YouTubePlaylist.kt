package eu.caraus.appsflastfm.services.youtube.model.youtube

import java.io.Serializable

data class YouTubePlaylist(

        var id: String? = "",

        var title: String? = "",

        var thumbnailURL: String? = "",

        var numberOfVideos: Long = 0,

        var status: String? = ""

) : Serializable
