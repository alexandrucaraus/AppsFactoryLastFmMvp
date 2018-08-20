package eu.caraus.appsflastfm.data.domain.lastFm.albums

import android.arch.persistence.room.Ignore
import com.google.gson.annotations.SerializedName

data class AlbumItem(

	@field:SerializedName("mbid")
	var mbid: String? = null,

	@field:SerializedName("image")
	var image: List<ImageItem?>? = null,

	@field:SerializedName("artist")
	var artist: Artist? = null,

	@field:SerializedName("playcount")
	var playcount: Int? = null,

	@field:SerializedName("name")
	var name: String? = null,

	@field:SerializedName("url")
	var url: String? = null,

	@Ignore
	var saved: Boolean? = false

)