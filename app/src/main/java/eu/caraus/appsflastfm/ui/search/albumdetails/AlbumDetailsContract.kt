package eu.caraus.appsflastfm.ui.search.albumdetails

import android.arch.lifecycle.LifecycleObserver
import eu.caraus.appsflastfm.common.retrofit.Outcome
import eu.caraus.appsflastfm.data.domain.lastFm.albuminfo.Album
import eu.caraus.appsflastfm.data.domain.lastFm.albuminfo.TrackItem
import eu.caraus.appsflastfm.services.youtube.model.youtube.YouTubeVideo
import eu.caraus.appsflastfm.ui.base.BaseContract
import io.reactivex.subjects.PublishSubject

interface AlbumDetailsContract : BaseContract {

    interface Presenter : BaseContract.BasePresenter<View>, LifecycleObserver {

        fun transitionHandler( enterPostponedTransition : () -> Unit )

        fun getAlbumInfo( artistName: String, albumName: String )

        fun triggerSeekTo( seekTo : Int )
        fun triggerPlayTrack( track : TrackItem)
        fun triggerStopTrack( track : TrackItem)

        fun goBack() : Boolean
    }

    interface View : BaseContract.BaseView {

        fun showAlbumInfo( album : Album? )

        fun updateTrackItem( youTubeVideo: YouTubeVideo)

        fun showLoading()
        fun hideLoading()

        fun showError( error : Throwable)

    }

    interface Interactor {
        fun getAlbumInfo( artistName: String, albumName: String )
        fun getAlbumInfoOutcome() : PublishSubject<Outcome<Album?>>
    }

    interface Navigator {
        fun goBack() : Boolean
    }

}