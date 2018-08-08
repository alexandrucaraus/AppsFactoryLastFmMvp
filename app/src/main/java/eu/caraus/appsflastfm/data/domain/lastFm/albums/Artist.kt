package eu.caraus.appsflastfm.data.domain.lastFm.albums

import com.google.gson.annotations.SerializedName

data class Artist(

	@field:SerializedName("mbid")
	val mbid: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("url")
	val url: String? = null
)