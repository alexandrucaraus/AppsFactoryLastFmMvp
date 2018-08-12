package eu.caraus.appsflastfm.ui.main.albumdetails

import android.arch.lifecycle.LifecycleObserver
import eu.caraus.appsflastfm.common.retrofit.Outcome
import eu.caraus.appsflastfm.data.domain.lastFm.albuminfo.Album
import eu.caraus.appsflastfm.data.domain.lastFm.albuminfo.TrackItem
import eu.caraus.appsflastfm.services.youtube.model.youtube.YouTubeVideo
import eu.caraus.appsflastfm.ui.base.BaseContract
import io.reactivex.subjects.PublishSubject

interface AlbumDetailsContract : BaseContract {

    interface Presenter : BaseContract.BasePresenter<View>, LifecycleObserver {

        fun triggerPlayTrack( track : TrackItem )
        fun triggerStopTrack( track : TrackItem)

        fun getAlbumInfo( artistId : String )
        fun goBack() : Boolean

    }

    interface View : BaseContract.BaseView {

        fun showAlbumInfo( album : Album? )

        fun showLoading()
        fun hideLoading()

        fun showError( error : Throwable )

        fun updateTrackItem( youTubeVideo: YouTubeVideo)


    }

    interface Interactor {
        fun getAlbumInfo( mbid : String )
        fun getAlbumInfoOutcome() : PublishSubject<Outcome<Album?>>
    }

    interface Navigator {

        fun playSong( youtubeUrl : YouTubeVideo)

        fun goBack() : Boolean
    }

}