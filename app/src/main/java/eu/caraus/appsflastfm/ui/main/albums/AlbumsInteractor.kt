package eu.caraus.appsflastfm.ui.main.albums

import eu.caraus.appsflastfm.common.extensions.addTo
import eu.caraus.appsflastfm.common.extensions.failed
import eu.caraus.appsflastfm.common.extensions.loading
import eu.caraus.appsflastfm.common.extensions.success
import eu.caraus.appsflastfm.common.retrofit.Outcome
import eu.caraus.appsflastfm.common.schedulers.SchedulerProvider
import eu.caraus.appsflastfm.data.domain.lastFm.albuminfo.Album
import eu.caraus.appsflastfm.data.local.Database
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable

import io.reactivex.subjects.PublishSubject


class AlbumsInteractor( private val database   : Database          ,
                        private val scheduler  : SchedulerProvider ,
                        private val disposable : CompositeDisposable ) : AlbumsContract.Interactor {

    private val albumsFetchResult = PublishSubject.create<Outcome<List<Album?>>>()
    private val albumDeleteResult = PublishSubject.create<Outcome<Boolean>>()

    override fun getAlbums(  ) {

        albumsFetchResult.loading(true)

        database.albumsDao()
                .selectAllFlowable()
                .subscribeOn( scheduler.io())
                .subscribe({
                        albumsFetchResult.success(it)
                    },{
                        albumsFetchResult.failed(it)
                })
                .addTo( disposable )
    }

    override fun deleteAlbum( album : Album ) {

      Observable.fromCallable { database.albumsDao().delete(album) }
                .subscribeOn( scheduler.io() )
                .subscribe (
                    {
                        if( it > 0)
                            albumDeleteResult.success(true )
                        else
                            albumDeleteResult.failed( Throwable("Nothing Deleted"))
                    },
                    {
                        albumDeleteResult.failed(it)
                })
                .addTo( disposable )
    }

    override fun getAlbumsOutcome(): PublishSubject<Outcome<List<Album?>>> = albumsFetchResult

    override fun deleteAlbumOutcome() : PublishSubject<Outcome<Boolean>> = albumDeleteResult

}