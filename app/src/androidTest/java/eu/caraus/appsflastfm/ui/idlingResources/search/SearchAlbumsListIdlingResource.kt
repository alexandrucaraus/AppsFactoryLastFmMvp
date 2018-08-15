package eu.caraus.appsflastfm.ui.idlingResources.search

import android.support.test.InstrumentationRegistry
import android.support.test.espresso.IdlingResource
import android.view.View
import android.widget.LinearLayout
import eu.caraus.appsflastfm.R
import eu.caraus.appsflastfm.testUtils.TestApplication
import eu.caraus.appsflastfm.ui.base.BaseActivity
import eu.caraus.appsflastfm.ui.base.BaseFragment
import eu.caraus.appsflastfm.ui.search.albums.AlbumsFragment

class SearchAlbumsListIdlingResource : IdlingResource {

    private var resourceCallback: IdlingResource.ResourceCallback? = null
    private var isIdle: Boolean = false

    override fun getName(): String = SearchArtistListIdlingResource::class.java.name

    override fun isIdleNow(): Boolean {

        if( isIdle )return true

        val activity = getCurrentActivity()

        val fragment = activity?.supportFragmentManager?.findFragmentById( R.id.search_fragment_container ) as BaseFragment?

        fragment?.let {

            isIdle = ( it is AlbumsFragment ) && it.activity?.findViewById<LinearLayout>(R.id.llAlbum)?.visibility == View.VISIBLE

        } ?: run {
            isIdle = false
        }

        if (isIdle) {
            resourceCallback?.onTransitionToIdle()
        }

        return isIdle
    }

    override fun registerIdleTransitionCallback( callback: IdlingResource.ResourceCallback? ) {
        this.resourceCallback = callback
    }

    private fun getCurrentActivity(): BaseActivity? {
        return (InstrumentationRegistry.getTargetContext().applicationContext as TestApplication).currentActivity
    }

}