package eu.caraus.appsflastfm.data.domain.lastFm.artists

import com.google.gson.annotations.SerializedName

data class ArtistItem(

	@field:SerializedName("image")
	val image: List<ImageItem?>? = null,

	@field:SerializedName("mbid")
	val mbid: String? = null,

	@field:SerializedName("listeners")
	val listeners: String? = null,

	@field:SerializedName("streamable")
	val streamable: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("url")
	val url: String? = null
)