package eu.caraus.appsflastfm.ui.search.artists

import eu.caraus.appsflastfm.common.extensions.failed
import eu.caraus.appsflastfm.common.extensions.loading
import eu.caraus.appsflastfm.common.extensions.subOnIoObsOnUi
import eu.caraus.appsflastfm.common.extensions.success
import eu.caraus.appsflastfm.common.retrofit.Outcome
import eu.caraus.appsflastfm.common.schedulers.SchedulerProvider
import eu.caraus.appsflastfm.data.domain.lastFm.artists.ArtistItem
import eu.caraus.appsflastfm.data.remote.lastFm.LastFmApi
import io.reactivex.subjects.PublishSubject

class ArtistsInteractor( private val service   : LastFmApi ,
                         private val scheduler : SchedulerProvider ) : ArtistsContract.Interactor {


    private val artistsFetchResult = PublishSubject.create<Outcome<List<ArtistItem?>>>()

    override fun getArtists( artistName : String ) {

        artistsFetchResult.loading(true )

        service.searchArtists( artistName ).subOnIoObsOnUi( scheduler)
                .subscribe({
                    it.results?.artistmatches?.artist?.let {
                        artistsFetchResult.success( it )
                    } ?: run {
                        artistsFetchResult.success( mutableListOf() )
                    }
                },{
                    artistsFetchResult.failed( it )
                })
    }

    override fun getArtistsOutcome() : PublishSubject<Outcome<List<ArtistItem?>>> {
        return artistsFetchResult
    }

}