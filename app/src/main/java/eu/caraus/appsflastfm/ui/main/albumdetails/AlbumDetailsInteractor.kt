package eu.caraus.appsflastfm.ui.main.albumdetails

import eu.caraus.appsflastfm.common.extensions.addTo
import eu.caraus.appsflastfm.common.extensions.failed
import eu.caraus.appsflastfm.common.extensions.loading
import eu.caraus.appsflastfm.common.extensions.success
import eu.caraus.appsflastfm.common.retrofit.Outcome
import eu.caraus.appsflastfm.common.schedulers.SchedulerProvider
import eu.caraus.appsflastfm.data.domain.lastFm.albuminfo.Album
import eu.caraus.appsflastfm.data.local.Database
import io.reactivex.disposables.CompositeDisposable

import io.reactivex.subjects.PublishSubject

class AlbumDetailsInteractor( private val database   : Database,
                              private val scheduler  : SchedulerProvider,
                              private val disposable : CompositeDisposable)

    : AlbumDetailsContract.Interactor {

    private val albumInfoFetchResult = PublishSubject.create<Outcome<Album?>>()

    override fun getAlbumInfo( mbid : String) {

        albumInfoFetchResult.loading(true)

        database.albumsDao().selectAlbumWithTracks( mbid )
                            .subscribeOn( scheduler.io())
                            .observeOn( scheduler.io())
                            .subscribe({
                                albumInfoFetchResult.success(it)
                            },{
                                albumInfoFetchResult.failed(it)
                            }).addTo( disposable )

    }

    override fun getAlbumInfoOutcome(): PublishSubject<Outcome<Album?>> {
        return albumInfoFetchResult
    }


}