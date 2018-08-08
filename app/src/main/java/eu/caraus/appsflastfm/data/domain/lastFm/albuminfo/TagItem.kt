package eu.caraus.appsflastfm.data.domain.lastFm.albuminfo


import com.google.gson.annotations.SerializedName

data class TagItem(

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("url")
	val url: String? = null
)