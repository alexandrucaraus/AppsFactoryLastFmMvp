package eu.caraus.appsflastfm.ui.main.albums

import eu.caraus.appsflastfm.common.extensions.failed
import eu.caraus.appsflastfm.common.extensions.loading
import eu.caraus.appsflastfm.common.extensions.subOnIoObsOnUi
import eu.caraus.appsflastfm.common.extensions.success
import eu.caraus.appsflastfm.common.retrofit.Outcome
import eu.caraus.appsflastfm.common.schedulers.SchedulerProvider
import eu.caraus.appsflastfm.data.domain.lastFm.albuminfo.Album
import eu.caraus.appsflastfm.data.local.Database

import io.reactivex.subjects.PublishSubject

class AlbumsInteractor( private val database  : Database         ,
                        private val scheduler : SchedulerProvider ) : AlbumsContract.Interactor {

    private val albumsFetchResult = PublishSubject.create<Outcome<List<Album?>>>()

    override fun getAlbums(  ) {

        albumsFetchResult.loading(true)

        database.albumsDao().selectAll().subOnIoObsOnUi( scheduler).subscribe(
                {
                    albumsFetchResult.success(it)
                },{
                    albumsFetchResult.failed(it)
                })
    }

    override fun getAlbumsOutcome(): PublishSubject<Outcome<List<Album?>>> {
        return albumsFetchResult
    }

}