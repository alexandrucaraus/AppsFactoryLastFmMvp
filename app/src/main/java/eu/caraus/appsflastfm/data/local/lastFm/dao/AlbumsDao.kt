package eu.caraus.appsflastfm.data.local.lastFm.dao

import android.arch.persistence.room.*
import eu.caraus.appsflastfm.data.domain.lastFm.albuminfo.Album
import eu.caraus.appsflastfm.data.domain.lastFm.albuminfo.Artist
import eu.caraus.appsflastfm.data.domain.lastFm.albuminfo.TrackItem
import eu.caraus.appsflastfm.data.domain.lastFm.albuminfo.Tracks
import eu.caraus.appsflastfm.data.domain.extensions.lastFm.AlbumWithStuff
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.Observable
import java.util.concurrent.Callable

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

    @Query( "SELECT * FROM albums_table WHERE mbid=:mbid")
    abstract fun select(mbid : String ) : Album

    @Query("SELECT * FROM albums_table")
    abstract fun selectAll() : List<Album>

    @Query( "SELECT * FROM albums_table WHERE mbid=:mbid")
    abstract fun selectFlowable(mbid : String ) : Flowable<Album>

    @Query("SELECT * FROM albums_table")
    abstract fun selectAllFlowable() : Flowable<List<Album>>

    @Transaction
    @Query( "SELECT * FROM albums_table WHERE mbid=:mbid")
    abstract fun selectAlbumWithStuff( mbid: String)  : AlbumWithStuff

    @Transaction
    @Query( "SELECT * FROM albums_table ")
    abstract fun selectAllAlbumsWithStuff()  : List<AlbumWithStuff>

    fun selectAlbumWithTracks( mbid: String ) : Flowable<Album> {
        return Observable.fromCallable( Callable {

            val stuff = selectAlbumWithStuff(mbid)
            val tracks = Tracks().apply {
                track = stuff.tracks
            }
            stuff.album.tracks = tracks
            return@Callable stuff.album
        }).toFlowable( BackpressureStrategy.LATEST )
    }

    fun selectAlbumsWithTracks() : Flowable<List<Album>> {
        return Observable.fromIterable( selectAllAlbumsWithStuff()).map {
            val tracks = Tracks().apply {
                track = it.tracks
            }
            it.album.tracks = tracks
            return@map it.album
        }.toList().toFlowable()
    }

    // Deletes

    @Delete
    abstract fun deleteAll( list: List<Album> )

    @Transaction
    @Delete
    abstract fun delete( person: Album ) : Int

}