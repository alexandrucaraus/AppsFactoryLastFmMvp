package eu.caraus.appsflastfm.ui.main.albums

import android.arch.lifecycle.LifecycleObserver
import eu.caraus.appsflastfm.common.retrofit.Outcome
import eu.caraus.appsflastfm.data.domain.lastFm.albuminfo.Album
import eu.caraus.appsflastfm.ui.base.BaseContract
import io.reactivex.subjects.PublishSubject

interface AlbumsContract : BaseContract {

    interface Presenter : BaseContract.BasePresenter<View>, LifecycleObserver {

        fun showAlbumDetails( mbid : String )
        fun deleteAlbum( album : Album )
        fun goBack() : Boolean

    }

    interface View : BaseContract.BaseView {

        fun updateAlbums(albums : List<Album?>)
        fun showFoundNothing()

        fun showLoading()
        fun hideLoading()

        fun deleteSuccess()
        fun deleteFailed()

        fun showError( error : Throwable )
    }

    interface Interactor {

        fun getAlbums ()
        fun getAlbumsOutcome () : PublishSubject<Outcome<List<Album?>>>

        fun deleteAlbum( album : Album )
        fun deleteAlbumOutcum() : PublishSubject<Outcome<Boolean>>

    }

    interface Navigator {
        fun showAlbumDetails( mbid : String )
        fun goBack() : Boolean
    }

}