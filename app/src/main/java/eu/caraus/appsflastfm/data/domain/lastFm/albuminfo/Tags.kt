package eu.caraus.appsflastfm.data.domain.lastFm.albuminfo


import com.google.gson.annotations.SerializedName

data class Tags(

	@field:SerializedName("tag")
	val tag: List<TagItem?>? = null
)