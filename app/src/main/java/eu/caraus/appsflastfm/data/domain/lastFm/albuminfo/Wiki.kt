package eu.caraus.appsflastfm.data.domain.lastFm.albuminfo

import com.google.gson.annotations.SerializedName

data class Wiki(

	@field:SerializedName("summary")
	val summary: String? = null,

	@field:SerializedName("published")
	val published: String? = null,

	@field:SerializedName("content")
	val content: String? = null
)