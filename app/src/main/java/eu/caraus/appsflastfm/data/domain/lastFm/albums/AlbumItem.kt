package eu.caraus.appsflastfm.data.domain.lastFm.albums

import android.arch.persistence.room.Ignore
import com.google.gson.annotations.SerializedName

data class AlbumItem(

	@field:SerializedName("mbid")
	val mbid: String? = null,

	@field:SerializedName("image")
	val image: List<ImageItem?>? = null,

	@field:SerializedName("artist")
	val artist: Artist? = null,

	@field:SerializedName("playcount")
	val playcount: Int? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("url")
	val url: String? = null,

	@Ignore
	var saved: Boolean? = false

)