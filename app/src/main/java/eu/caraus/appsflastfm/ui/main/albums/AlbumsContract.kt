package eu.caraus.appsflastfm.ui.main.albums

import android.arch.lifecycle.LifecycleObserver
import eu.caraus.appsflastfm.common.retrofit.Outcome
import eu.caraus.appsflastfm.data.domain.lastFm.albuminfo.Album
import eu.caraus.appsflastfm.data.domain.lastFm.albums.AlbumItem
import eu.caraus.appsflastfm.ui.base.BaseContract
import io.reactivex.subjects.PublishSubject

interface AlbumsContract : BaseContract {

    interface Presenter : BaseContract.BasePresenter<View>, LifecycleObserver {

        fun showAlbumDetails( mbid : String )
        fun goBack() : Boolean

    }

    interface View : BaseContract.BaseView {

        fun showFoundAlbums( artists : List<Album?>)
        fun showFoundNothing()

        fun showLoading()
        fun hideLoading()

        fun showError( error : Throwable )
    }

    interface Interactor {
        fun getAlbums ()
        fun getAlbumsOutcome () : PublishSubject<Outcome<List<Album?>>>
    }

    interface Navigator {
        fun showAlbumDetails( mbid : String )
        fun goBack() : Boolean
    }

}