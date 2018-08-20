package eu.caraus.appsflastfm.data.domain.lastFm.artists

import com.google.gson.annotations.SerializedName

data class ArtistItem(

	@field:SerializedName("image")
	var image: List<ImageItem?>? = null,

	@field:SerializedName("mbid")
	var mbid: String? = null,

	@field:SerializedName("listeners")
	var listeners: String? = null,

	@field:SerializedName("streamable")
	var streamable: String? = null,

	@field:SerializedName("name")
	var name: String? = null,

	@field:SerializedName("url")
	var url: String? = null
)