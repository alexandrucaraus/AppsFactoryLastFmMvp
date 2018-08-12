package eu.caraus.appsflastfm.ui.main.albumdetails

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.OnLifecycleEvent
import eu.caraus.appsflastfm.common.bus.RxBus
import eu.caraus.appsflastfm.common.extensions.subOnIoObsOnUi
import eu.caraus.appsflastfm.common.retrofit.Outcome
import eu.caraus.appsflastfm.common.schedulers.SchedulerProvider
import eu.caraus.appsflastfm.data.domain.extensions.lastFm.TrackState
import eu.caraus.appsflastfm.data.domain.lastFm.albuminfo.Album
import eu.caraus.appsflastfm.data.domain.lastFm.albuminfo.TrackItem
import eu.caraus.appsflastfm.services.youtube.busevents.client.ActionPlay
import eu.caraus.appsflastfm.services.youtube.busevents.client.ActionStop
import eu.caraus.appsflastfm.services.youtube.busevents.client.ActionUpdate
import eu.caraus.appsflastfm.services.youtube.busevents.service.PlayingUpdate
import eu.caraus.appsflastfm.services.youtube.busevents.service.StoppedUpdate
import eu.caraus.appsflastfm.services.youtube.busevents.service.ElapsedUpdate
import eu.caraus.appsflastfm.services.youtube.model.lastFm.ExtractYoutubeUrlFromLastFm
import eu.caraus.appsflastfm.services.youtube.model.youtube.YouTubeVideo
import io.reactivex.BackpressureStrategy
import io.reactivex.Observable
import io.reactivex.disposables.Disposable

class AlbumDetailsPresenter( private val interactor: AlbumDetailsContract.Interactor,
                             private val navigator: AlbumDetailsContract.Navigator,
                             private val scheduler : SchedulerProvider,
                             private val rxBus : RxBus ) : AlbumDetailsContract.Presenter {

    private var view : AlbumDetailsContract.View? = null

    private var disposable : Disposable? = null

    private var trackDisposable : Disposable? = null


    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreate(){

        disposable = interactor.getAlbumInfoOutcome().subOnIoObsOnUi(scheduler).subscribe {
            when( it ){
                is Outcome.Progress ->
                    if( it.loading ) showLoading() else hideLoading()
                is Outcome.Failure  ->
                    showError( it.error )
                is Outcome.Success  -> showAlbumInfo( it.data )
            }
        }

        rxBus.service().subOnIoObsOnUi(scheduler).subscribe {
            when( it ){
                is PlayingUpdate -> updateTrackPlaying( it.youTubeVideo )
                is StoppedUpdate -> updateTrackStopped( it.youTubeVideo )
                is ElapsedUpdate -> updateTrackElapsed( it.youTubeVideo , it.elapsed )
            }
        }

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResume(){

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy(){
        disposable?.dispose()
    }

    override fun triggerPlayTrack(track : TrackItem ) {

        trackDisposable?.dispose()

        trackDisposable = track.url?.let {
            Observable.fromCallable {
                return@fromCallable ExtractYoutubeUrlFromLastFm().extract(it)
            }.toFlowable(BackpressureStrategy.DROP).subOnIoObsOnUi(scheduler)
                    .subscribe { video ->
                video?.let {
                    it.trackId = track.id
                    rxBus.sentEventToService( ActionPlay().apply { this.youTubeVideo = it } )
                }
            }
        }

    }

    override fun triggerStopTrack( track: TrackItem ) {
        rxBus.sentEventToService( ActionStop() )
    }

    private fun updateTrackPlaying( youTubeVideo : YouTubeVideo ) {
        youTubeVideo.trackState = TrackState.PLAYING
        view?.updateTrackItem( youTubeVideo )
    }

    private fun updateTrackStopped( youTubeVideo : YouTubeVideo ) {
        youTubeVideo.trackState = TrackState.STOPPED
        view?.updateTrackItem( youTubeVideo )
    }

    private fun updateTrackElapsed( youTubeVideo: YouTubeVideo , elapsed : Int ) {
        youTubeVideo.trackState = TrackState.PLAYING
        youTubeVideo.trackElapsed = elapsed
        view?.updateTrackItem( youTubeVideo )
    }

    override fun getAlbumInfo( mbid: String ) {
        interactor.getAlbumInfo( mbid )
    }

    private fun showAlbumInfo( album : Album?) {
        view?.showAlbumInfo( album )
    }

    private fun hideLoading() {
        view?.hideLoading()
    }

    private fun showLoading() {
        view?.showLoading()
    }

    private fun showError( error: Throwable) {
        view?.showError(error)
    }

    override fun goBack(): Boolean {
       return navigator.goBack()
    }

    override fun onViewAttached(view: AlbumDetailsContract.View) {
        this.view = view
    }

    override fun onViewDetached(detach: Boolean) {
        this.view = null
    }


}