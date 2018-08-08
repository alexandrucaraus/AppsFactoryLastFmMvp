package eu.caraus.appsflastfm.data.domain.lastFm.albuminfo

import com.google.gson.annotations.SerializedName

data class Tracks(

	@field:SerializedName("track")
	val track: List<TrackItem?>? = null
)