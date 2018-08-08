package eu.caraus.appsflastfm.data.domain.lastFm.albuminfo


import com.google.gson.annotations.SerializedName

data class Streamable(

	@field:SerializedName("#text")
	val text: String? = null,

	@field:SerializedName("fulltrack")
	val fulltrack: String? = null
)