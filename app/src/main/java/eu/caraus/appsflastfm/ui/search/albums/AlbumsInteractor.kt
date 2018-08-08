package eu.caraus.appsflastfm.ui.search.albums


import eu.caraus.appsflastfm.common.extensions.failed
import eu.caraus.appsflastfm.common.extensions.loading
import eu.caraus.appsflastfm.common.extensions.subOnIoObsOnUi
import eu.caraus.appsflastfm.common.extensions.success
import eu.caraus.appsflastfm.common.retrofit.Outcome
import eu.caraus.appsflastfm.common.schedulers.SchedulerProvider
import eu.caraus.appsflastfm.data.domain.lastFm.albums.AlbumItem
import eu.caraus.appsflastfm.data.local.Database
import eu.caraus.appsflastfm.data.remote.lastFm.LastFmApi
import io.reactivex.subjects.PublishSubject
import kotlin.concurrent.thread

class AlbumsInteractor(private val service   : LastFmApi,
                       private val database  : Database,
                       private val scheduler : SchedulerProvider ) : AlbumsContract.Interactor {

    private val albumsFetchResult = PublishSubject.create<Outcome<List<AlbumItem?>>>()

    override fun getAlbums( artistName : String ) {

        albumsFetchResult.loading(true)

        service.getArtistTopAlbums( artistName ).subOnIoObsOnUi( scheduler)
                .subscribe({

                    it.topalbums?.album?.let { list->

                        albumsFetchResult.success(list)

                    } ?: run {
                        albumsFetchResult.success(mutableListOf())
                    }

                },{
                    albumsFetchResult.failed(it)
                })

    }

    override fun getAlbumsOutcome(): PublishSubject<Outcome<List<AlbumItem?>>> {
        return albumsFetchResult
    }

    override fun saveAlbum( artistName: String, albumName: String) {

        // TODO : Handle error

        service.getArtistAlbumInfo( artistName, albumName )
                .subscribeOn( scheduler.io())
                .subscribe { response->
                    response.album?.let {
                        database.albumsDao().insertWhole(it)
                    }
                }
    }

}