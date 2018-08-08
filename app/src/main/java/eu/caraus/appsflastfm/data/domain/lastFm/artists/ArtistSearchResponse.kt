package eu.caraus.appsflastfm.data.domain.lastFm.artists

import com.google.gson.annotations.SerializedName

data class ArtistSearchResponse(

	@field:SerializedName("results")
	val results: Results? = null
)