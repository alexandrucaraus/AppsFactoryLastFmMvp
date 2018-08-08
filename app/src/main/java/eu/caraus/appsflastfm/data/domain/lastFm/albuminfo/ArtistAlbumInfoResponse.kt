package eu.caraus.appsflastfm.data.domain.lastFm.albuminfo

import com.google.gson.annotations.SerializedName

data class ArtistAlbumInfoResponse(

	@field:SerializedName("album")
	val album: Album? = null
)