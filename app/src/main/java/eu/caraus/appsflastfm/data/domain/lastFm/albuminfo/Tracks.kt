package eu.caraus.appsflastfm.data.domain.lastFm.albuminfo

import com.google.gson.annotations.SerializedName

data class Tracks(

	@field:SerializedName("track")
	var track: List<TrackItem?>? = null
)