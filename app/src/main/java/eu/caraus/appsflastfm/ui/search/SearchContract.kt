package eu.caraus.appsflastfm.ui.search

import android.arch.lifecycle.LifecycleObserver
import eu.caraus.appsflastfm.ui.base.BaseContract

interface SearchContract : BaseContract {

    interface Presenter : BaseContract.BasePresenter<View>, LifecycleObserver {

        fun searchArtist( artist : String)

        fun goBack() : Boolean
    }

    interface View : BaseContract.BaseView

    interface Interactor

    interface Navigator {

        fun showArtists( artist : String )

        fun goBack() : Boolean

    }

}