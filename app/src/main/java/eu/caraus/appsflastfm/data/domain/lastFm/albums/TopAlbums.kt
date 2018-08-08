package eu.caraus.appsflastfm.data.domain.lastFm.albums

import com.google.gson.annotations.SerializedName

data class TopAlbums(

	@field:SerializedName("@attr")
	val attr: Attr? = null,

	@field:SerializedName("album")
	val album: List<AlbumItem?>? = null
)