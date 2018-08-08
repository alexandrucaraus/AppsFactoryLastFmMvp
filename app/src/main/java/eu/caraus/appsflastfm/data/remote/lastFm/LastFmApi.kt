package eu.caraus.appsflastfm.data.remote.lastFm

import eu.caraus.appsflastfm.data.domain.lastFm.albuminfo.ArtistAlbumInfoResponse
import eu.caraus.appsflastfm.data.domain.lastFm.albums.ArtistTopAlbumsResponse
import eu.caraus.appsflastfm.data.domain.lastFm.artists.ArtistSearchResponse
import io.reactivex.Flowable
import retrofit2.http.GET
import retrofit2.http.Query

/**
 *  LastFmApi service interface for Retrofit
 *
 */

interface LastFmApi {

    @GET("/2.0/?method=artist.search")
    fun searchArtists( @Query("artist") artist : String )        : Flowable<ArtistSearchResponse>

    @GET("/2.0/?method=artist.gettopalbums")
    fun getArtistTopAlbums( @Query("artist") artist : String )   : Flowable<ArtistTopAlbumsResponse>

    @GET("/2.0/?method=album.getinfo")
    fun getArtistAlbumInfo( @Query("artist" ) artist : String  ,
                            @Query("album"  ) album  : String  ) : Flowable<ArtistAlbumInfoResponse>
}
