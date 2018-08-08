package eu.caraus.appsflastfm.data.domain.lastFm.albuminfo

import android.arch.persistence.room.*
import com.google.gson.annotations.SerializedName
import eu.caraus.appsflastfm.data.local.lastFm.converters.ImageListTypeConverter


@Entity(

		tableName   = "albums_table",

		indices     = [( Index("mbid") )]

)
data class Album(

	@PrimaryKey
	@field:SerializedName("mbid")
	var mbid: String = "",

	@field:SerializedName("listeners")
	var listeners: String? = null,

	@field:SerializedName("artist")
	var artist: String? = null,

	@field:SerializedName("playcount")
	var playcount: String? = null,

	@field:SerializedName("name")
	var name: String? = null,

	@field:SerializedName("url")
	var url: String? = null,

	@TypeConverters( ImageListTypeConverter::class)
	@field:SerializedName("image")
	var image: List<ImageItem?>? = null,

	@Ignore
	@field:SerializedName("wiki")
	var wiki: Wiki? = null,

	@Ignore
	@field:SerializedName("tracks")
	var tracks: Tracks? = null,

	@Ignore
	@field:SerializedName("tags")
	var tags: Tags? = null
)