package eu.caraus.appsflastfm.ui.search.artists

import android.arch.lifecycle.LifecycleObserver
import eu.caraus.appsflastfm.common.retrofit.Outcome
import eu.caraus.appsflastfm.data.domain.lastFm.artists.ArtistItem
import eu.caraus.appsflastfm.ui.base.BaseContract
import io.reactivex.subjects.PublishSubject

interface ArtistsContract {

    interface Presenter : BaseContract.BasePresenter<View>, LifecycleObserver {

        fun searchArtist( artistName : String )

        fun showTopAlbums( artistId : String )

        fun goBack() : Boolean

    }

    interface View : BaseContract.BaseView {

        fun showFoundArtists( artists : List<ArtistItem?>)

        fun showList()
        fun hideList()

        fun showPlaceholder()
        fun hidePlaceholder()

        fun showLoading()
        fun hideLoading()

        fun showError( error : Throwable )

    }

    interface Interactor {

        fun getArtists( artistName : String )
        fun getArtistsOutcome() : PublishSubject<Outcome<List<ArtistItem?>>>

    }

    interface Navigator {

        fun showTopAlbums( artistId : String )

        fun goBack() : Boolean

    }

}