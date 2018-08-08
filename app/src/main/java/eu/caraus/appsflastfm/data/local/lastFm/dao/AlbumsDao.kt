package eu.caraus.appsflastfm.data.local.lastFm.dao

import android.arch.persistence.room.*
import eu.caraus.appsflastfm.data.domain.lastFm.albuminfo.Album
import eu.caraus.appsflastfm.data.domain.lastFm.albuminfo.Artist
import eu.caraus.appsflastfm.data.domain.lastFm.albuminfo.TrackItem
import io.reactivex.Flowable

@Dao
abstract class AlbumsDao {

    // Inserts

    @Transaction
    @Insert( onConflict = OnConflictStrategy.REPLACE)
    fun insertWhole( album: Album ) {

        insert( album )

        album.tracks?.track?.forEach { item ->

            item?.let { track ->

                track.mbidAlbumFk = album.mbid

                val trackId = insert( track )

            track.artist?.let { artist ->

                    artist.tracks_id_fk = trackId

                    insert( artist )

                }
            }
        }
    }

    @Insert( onConflict = OnConflictStrategy.REPLACE )
    abstract fun insert( album : Album )

    @Insert( onConflict = OnConflictStrategy.REPLACE )
    abstract fun insert( track : TrackItem ) : Long

    @Insert( onConflict = OnConflictStrategy.REPLACE )
    abstract fun insert( artist : Artist )

    // Updates

    // ...

    // Selects

    @Query("SELECT * FROM albums_table")
    abstract fun selectAll() : Flowable<List<Album>>

    // Deletes

    @Delete
    abstract fun deleteAll( list: List<Album> )

    @Transaction
    @Delete
    abstract fun delete( person: Album )

}