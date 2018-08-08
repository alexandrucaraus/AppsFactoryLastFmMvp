package eu.caraus.appsflastfm.data.domain.lastFm.albums

import com.google.gson.annotations.SerializedName

data class Attr(

	@field:SerializedName("total")
	val total: String? = null,

	@field:SerializedName("perPage")
	val perPage: String? = null,

	@field:SerializedName("artist")
	val artist: String? = null,

	@field:SerializedName("totalPages")
	val totalPages: String? = null,

	@field:SerializedName("page")
	val page: String? = null
)