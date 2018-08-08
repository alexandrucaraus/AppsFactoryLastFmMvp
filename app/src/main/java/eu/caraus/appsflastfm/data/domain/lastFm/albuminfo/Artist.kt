package eu.caraus.appsflastfm.data.domain.lastFm.albuminfo

import android.arch.persistence.room.*
import com.google.gson.annotations.SerializedName

@Entity(

		tableName   = "artists_table",

		indices     = [ (Index("mbid")), Index(("tracks_id_fk"))],

		foreignKeys = [( ForeignKey( entity = TrackItem::class,
									 parentColumns = ["id"],
									 childColumns  = ["tracks_id_fk"],
									 onDelete = ForeignKey.CASCADE )
					  )]

)

data class Artist(

	@PrimaryKey
	@field:SerializedName("mbid")
	var mbid: String = "",

	@ColumnInfo( name = "tracks_id_fk")
	var tracks_id_fk : Long,

	@field:SerializedName("name")
	var name: String? = null,

	@field:SerializedName("url")
	var url: String? = null
) {
	@Ignore constructor() : this("", 0, "", "")
}