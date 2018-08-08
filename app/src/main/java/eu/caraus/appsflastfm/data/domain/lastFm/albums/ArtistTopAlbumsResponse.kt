package eu.caraus.appsflastfm.data.domain.lastFm.albums

import com.google.gson.annotations.SerializedName

data class ArtistTopAlbumsResponse(

	@field:SerializedName("topalbums")
	val topalbums: TopAlbums? = null

)