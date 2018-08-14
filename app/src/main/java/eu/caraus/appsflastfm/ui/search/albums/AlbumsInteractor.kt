package eu.caraus.appsflastfm.ui.search.albums



import eu.caraus.appsflastfm.common.extensions.failed
import eu.caraus.appsflastfm.common.extensions.loading
import eu.caraus.appsflastfm.common.extensions.subOnIoObsOnUi
import eu.caraus.appsflastfm.common.extensions.success
import eu.caraus.appsflastfm.common.retrofit.Outcome
import eu.caraus.appsflastfm.common.schedulers.SchedulerProvider
import eu.caraus.appsflastfm.data.domain.lastFm.albuminfo.Album
import eu.caraus.appsflastfm.data.domain.lastFm.albums.AlbumItem
import eu.caraus.appsflastfm.data.domain.lastFm.albums.ArtistTopAlbumsResponse
import eu.caraus.appsflastfm.data.local.Database
import eu.caraus.appsflastfm.data.remote.lastFm.LastFmApi
import io.reactivex.Flowable.zip
import io.reactivex.functions.BiFunction

import io.reactivex.subjects.PublishSubject


class AlbumsInteractor(private val service   : LastFmApi,
                       private val database  : Database,
                       private val scheduler : SchedulerProvider ) : AlbumsContract.Interactor {

    private val albumsFetchResult = PublishSubject.create<Outcome<List<AlbumItem?>>>()
    private val albumSaveResult = PublishSubject.create<Outcome<Album>>()

    override fun getAlbums( artistName : String ) {

        albumsFetchResult.loading(true)

        zip( service.getArtistTopAlbums( artistName),
             database.albumsDao().selectAllFlowable(),
                BiFunction<ArtistTopAlbumsResponse,List<Album>,List<AlbumItem?>> {
                    topAlbums, listAlbums -> merge( topAlbums, listAlbums)
                }
               ).subOnIoObsOnUi( scheduler)
                .subscribe( {
                    albumsFetchResult.success(it)
                    },{
                    albumsFetchResult.failed(it)
                })

    }

    private fun merge( searchedAlbums: ArtistTopAlbumsResponse, savedAlbums: List<Album> ) : List<AlbumItem?> {

        lateinit var result : List<AlbumItem?>

        searchedAlbums.topalbums?.album?.let {
            result = it
        } ?: run {
            result = listOf()
        }

        val hash = mutableMapOf<String,Boolean>()

        savedAlbums.forEach {
            hash[ it.mbid ] = true
        }

        result.forEach {

            if( hash.containsKey( it?.mbid ) ) {
                it?.saved = true
                hash.remove( it?.mbid )
            }

            if( hash.isEmpty() ) return@forEach
        }

        return result
    }

    override fun getAlbumsOutcome(): PublishSubject<Outcome<List<AlbumItem?>>> {
        return albumsFetchResult
    }

    override fun saveAlbum( artistName: String, albumName: String) {

        service.getArtistAlbumInfo( artistName, albumName )
                .subscribeOn( scheduler.io())
                .subscribe ({
                    albums ->
                        albums.album?.let {
                            database.albumsDao().insertWhole(it)
                            albumSaveResult.success(it)
                        }
                },{
                       albumSaveResult.failed(it)
                })
    }

    override fun getAlbumSaveOutcome(): PublishSubject<Outcome<Album>> {
        return albumSaveResult
    }

}