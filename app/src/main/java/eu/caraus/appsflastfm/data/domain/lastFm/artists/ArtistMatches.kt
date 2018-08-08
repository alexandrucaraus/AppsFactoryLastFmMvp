package eu.caraus.appsflastfm.data.domain.lastFm.artists

import com.google.gson.annotations.SerializedName

data class ArtistMatches(

	@field:SerializedName("artist")
	val artist: List<ArtistItem?>? = null
)