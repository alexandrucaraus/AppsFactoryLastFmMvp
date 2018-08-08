package eu.caraus.appsflastfm.ui.main.albumdetails

import eu.caraus.appsflastfm.common.extensions.failed
import eu.caraus.appsflastfm.common.extensions.loading
import eu.caraus.appsflastfm.common.extensions.subOnIoObsOnUi
import eu.caraus.appsflastfm.common.extensions.success
import eu.caraus.appsflastfm.common.retrofit.Outcome
import eu.caraus.appsflastfm.common.schedulers.SchedulerProvider
import eu.caraus.appsflastfm.data.domain.lastFm.albuminfo.Album
import eu.caraus.appsflastfm.data.remote.lastFm.LastFmApi
import io.reactivex.subjects.PublishSubject

class AlbumDetailsInteractor( private val service   : LastFmApi         ,
                              private val scheduler : SchedulerProvider )

    : AlbumDetailsContract.Interactor {

    private val albumInfoFetchResult = PublishSubject.create<Outcome<Album?>>()

    override fun getAlbumInfo( artistName: String, albumName : String) {

        albumInfoFetchResult.loading(true)

        service.getArtistAlbumInfo( artistName, albumName ).subOnIoObsOnUi( scheduler)
                .subscribe({
                    it.album?.let {
                        albumInfoFetchResult.success(it)
                    } ?: run {
                        albumInfoFetchResult.failed( Throwable("No Details For The Album"))
                    }
                },{
                    albumInfoFetchResult.failed(it)
                })

    }

    override fun getAlbumInfoOutcome(): PublishSubject<Outcome<Album?>> {
        return albumInfoFetchResult
    }


}