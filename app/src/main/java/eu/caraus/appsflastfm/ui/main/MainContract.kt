package eu.caraus.appsflastfm.ui.main

import android.arch.lifecycle.LifecycleObserver
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

        fun showSavedAlbumsScreen()

        fun showSearchResultScreen( string: String )

        fun goBack() : Boolean

    }

}