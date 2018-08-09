package eu.caraus.appsflastfm.data.domain.lastFmExtended

import android.arch.persistence.room.Embedded
import android.arch.persistence.room.Relation
import eu.caraus.appsflastfm.data.domain.lastFm.albuminfo.Album
import eu.caraus.appsflastfm.data.domain.lastFm.albuminfo.TrackItem

class AlbumWithStuff( @Embedded var album : Album) {

    @Relation( parentColumn = "mbid", entityColumn = "mbid_album_fk", entity = TrackItem::class)
    var tracks: List<TrackItem?>? = null

}