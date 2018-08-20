package eu.caraus.appsflastfm.ui.main

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.OnLifecycleEvent

/**
 *  MainPresenter - MainActivities presenter
 *
 *  @param - navigator, used to navigate from one activity to another,
 *           or between fragments
 *
 */

class MainPresenter( val navigator  : MainContract.Navigator   ) : MainContract.Presenter {


    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreate() {
        navigator.showSavedAlbumsScreen()
        navigator.startMusicService()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResume() {

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy(){
        navigator.stopMusicService()
    }

    override fun goBack(): Boolean {
        return navigator.goBack()
    }

    override fun onViewAttached( view: MainContract.View) {}

    override fun onViewDetached( detach: Boolean) {}

}