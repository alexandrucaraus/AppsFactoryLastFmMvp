package eu.caraus.appsflastfm.data.domain.lastFm.albums


import com.google.gson.annotations.SerializedName


data class ImageItem(

	@field:SerializedName("#text")
	val text: String? = null,

	@field:SerializedName("size")
	val size: String? = null
)