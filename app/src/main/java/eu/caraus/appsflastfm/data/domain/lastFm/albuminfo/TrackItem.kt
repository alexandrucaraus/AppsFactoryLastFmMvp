package eu.caraus.appsflastfm.data.domain.lastFm.albuminfo

import android.arch.persistence.room.*
import com.google.gson.annotations.SerializedName
import eu.caraus.appsflastfm.data.domain.extensions.lastFm.TrackState

@Entity(

		tableName   = "tracks_table",

		indices     = [ (Index( "id")) , (Index("mbid_album_fk")) ],

		foreignKeys = [( ForeignKey( entity = Album::class,
									 parentColumns = ["mbid"],
									 childColumns  = ["mbid_album_fk"],
									 onDelete = ForeignKey.CASCADE,
									 onUpdate = ForeignKey.CASCADE )
					   )]

)
data class TrackItem(

	@PrimaryKey( autoGenerate = true )
	var id : Long,

	@ColumnInfo( name = "mbid_album_fk" )
	var mbidAlbumFk : String,

	@field:SerializedName("duration")
	var duration: String? = null,

	@field:SerializedName("name")
	var name: String? = null,

	@field:SerializedName("url")
	var url: String? = null,

	@Ignore
	@field:SerializedName("artist")
	var artist: Artist? = null,

	@Ignore
	@field:SerializedName("@attr")
	var attr: Attr? = null,

	@Ignore
	@field:SerializedName("streamable")
	var streamable: Streamable? = null,

	@Ignore
	var trackState : TrackState = TrackState.STOPPED,

	@Ignore
	var trackElapsed: Int = 0

) {
	constructor() : this(0,"", "", "", "", null, null, null)
}