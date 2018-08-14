package eu.caraus.appsflastfm.ui.search.albums

import android.arch.lifecycle.LifecycleObserver
import android.widget.ImageView
import eu.caraus.appsflastfm.common.retrofit.Outcome
import eu.caraus.appsflastfm.data.domain.lastFm.albuminfo.Album
import eu.caraus.appsflastfm.data.domain.lastFm.albums.AlbumItem
import eu.caraus.appsflastfm.ui.base.BaseContract
import io.reactivex.subjects.PublishSubject

interface AlbumsContract : BaseContract {

    interface Presenter : BaseContract.BasePresenter<View>, LifecycleObserver {

        fun showAlbumDetails( artistName: String, albumName: String )
        fun showAlbumDetails( artistName: String, albumName: String , sharedElement : ImageView )
        fun showAlbumDetails( artistName: String, albumName: String ,
                              sharedPicUrl  : String, sharedElement : ImageView )

        fun saveAlbumDetails( artistName: String, albumName: String )

        fun getAlbums( artistId : String )
        fun goBack() : Boolean
    }

    interface View : BaseContract.BaseView {

        fun showFoundAlbums( albums : List<AlbumItem?>)

        fun showLoading()
        fun hideLoading()

        fun showPlaceholder()
        fun hidePlaceholder()

        fun showList()
        fun hideList()

        fun showMessage( msg : String )
        fun showError( error : Throwable )
    }

    interface Interactor {

        fun getAlbums ( albums : String )
        fun getAlbumsOutcome () : PublishSubject<Outcome<List<AlbumItem?>>>

        fun saveAlbum( artistName: String, albumName: String)
        fun getAlbumSaveOutcome() : PublishSubject<Outcome<Album>>

    }

    interface Navigator {

        fun showAlbumDetails( artistName : String, albumName : String )
        fun showAlbumDetails( artistName : String, albumName : String, sharedElement : ImageView )
        fun showAlbumDetails( artistName   : String, albumName     : String    ,
                              sharedPicUrl : String, sharedElement : ImageView )

        fun goBack() : Boolean
    }

}