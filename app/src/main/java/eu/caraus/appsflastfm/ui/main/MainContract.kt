package eu.caraus.appsflastfm.ui.main

import android.arch.lifecycle.LifecycleObserver
import eu.caraus.appsflastfm.ui.base.BaseActivity
import eu.caraus.appsflastfm.ui.base.BaseContract


/**
 *  Interface definitions for the MainActivity, which are part of MVP pattern
 */

interface MainContract : BaseContract {

    interface Presenter : BaseContract.BasePresenter<View>, LifecycleObserver {
        fun goBack() : Boolean
    }

    interface View : BaseContract.BaseView

    interface Navigator {

        fun startMusicService()
        fun stopMusicService()

        fun showSavedAlbumsScreen()

        fun showSearchResultScreen( searchTerm : String )
        fun showSearchResultScreenWithResult( activity: BaseActivity , searchTerm : String )

        fun goBack() : Boolean

    }

}