package eu.caraus.appsflastfm.ui.search.albumdetails

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
import eu.caraus.appsflastfm.services.youtube.busevents.client.ActionSeek
import eu.caraus.appsflastfm.services.youtube.busevents.client.ActionStop
import eu.caraus.appsflastfm.services.youtube.busevents.service.ElapsedUpdate
import eu.caraus.appsflastfm.services.youtube.busevents.service.PlayingUpdate
import eu.caraus.appsflastfm.services.youtube.busevents.service.StoppedUpdate
import eu.caraus.appsflastfm.services.youtube.model.lastFm.ExtractYoutubeUrlFromLastFm
import eu.caraus.appsflastfm.services.youtube.model.youtube.YouTubeVideo
import io.reactivex.BackpressureStrategy
import io.reactivex.Observable
import io.reactivex.disposables.Disposable

class AlbumDetailsPresenter( private val interactor: AlbumDetailsContract.Interactor,
                             private val navigator: AlbumDetailsContract.Navigator,
                             private val scheduler : SchedulerProvider,
                             private val rxBus : RxBus) : AlbumDetailsContract.Presenter {

    private var view : AlbumDetailsContract.View? = null

    private var albumDisposable : Disposable? = null
    private var trackDisposable : Disposable? = null

    private var album : Album? = null

    private var activateTransition = {}

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreate(){

        albumDisposable = interactor.getAlbumInfoOutcome().subOnIoObsOnUi(scheduler).subscribe {
            when( it ){
                is Outcome.Progress ->
                    if( it.loading ) showLoading() else hideLoading()
                is Outcome.Failure  ->
                    showError( it.error )
                is Outcome.Success  -> { album = it.data ; showAlbumInfo( it.data ) }
            }
        }

        rxBus.service().subOnIoObsOnUi(scheduler).subscribe {
            when( it ){
                is PlayingUpdate ->
                    updateTrackPlaying( it.youTubeVideo )
                is StoppedUpdate ->
                    updateTrackStopped( it.youTubeVideo )
                is ElapsedUpdate ->
                    updateTrackElapsed( it.youTubeVideo )
            }
        }

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResume(){
        album?.let {
            showAlbumInfo(it)
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy(){
        albumDisposable?.dispose()
        trackDisposable?.dispose()
        activateTransition = {}
    }

    override fun transitionHandler( activate: () -> Unit ) {
        activateTransition = activate
    }

    override fun getAlbumInfo( artistName: String, albumName: String) {
        album = null
        interactor.getAlbumInfo( artistName, albumName)
    }

    private fun showAlbumInfo( album : Album?) {
        activateTransition()
        activateTransition = {}
        view?.showAlbumInfo( album  )
    }

    override fun triggerPlayTrack(track : TrackItem ) {

        trackDisposable?.dispose()
        trackDisposable = track.url?.let {
            Observable.fromCallable { return@fromCallable ExtractYoutubeUrlFromLastFm().extract(it) }
                    .toFlowable(BackpressureStrategy.DROP)
                    .subOnIoObsOnUi(scheduler)
                    .subscribe { video ->
                        video?.let {
                            it.trackId = track.id
                            it.trackState = TrackState.STOPPED
                            it.trackElapsed = 0
                            rxBus.sentEventToService( ActionPlay().apply { this.youTubeVideo = it })
                        }
                    }}

    }

    override fun triggerStopTrack( track: TrackItem ) {
        rxBus.sentEventToService( ActionStop() )
    }

    override fun triggerSeekTo(seekTo: Int) {
        rxBus.sentEventToService( ActionSeek( seekTo))
    }

    private fun updateTrackPlaying( youTubeVideo : YouTubeVideo) {
        youTubeVideo.trackState = TrackState.PLAYING
        view?.updateTrackItem( youTubeVideo )
    }

    private fun updateTrackStopped( youTubeVideo : YouTubeVideo) {
        youTubeVideo.trackState = TrackState.STOPPED
        view?.updateTrackItem( youTubeVideo )
    }

    private fun updateTrackElapsed( youTubeVideo: YouTubeVideo) {
        youTubeVideo.trackState = TrackState.PLAYING
        view?.updateTrackItem( youTubeVideo )
    }

    private fun hideLoading() {

    }

    private fun showLoading() {

    }

    private fun showError( error: Throwable ) {

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