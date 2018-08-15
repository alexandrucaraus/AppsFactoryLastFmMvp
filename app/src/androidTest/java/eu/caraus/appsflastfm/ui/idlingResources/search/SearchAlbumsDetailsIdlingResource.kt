package eu.caraus.appsflastfm.ui.idlingResources.search

import android.support.test.InstrumentationRegistry
import android.support.test.espresso.IdlingResource
import android.support.v4.widget.NestedScrollView
import android.view.View
import android.widget.TextView
import eu.caraus.appsflastfm.R
import eu.caraus.appsflastfm.testUtils.TestApplication
import eu.caraus.appsflastfm.ui.base.BaseActivity
import eu.caraus.appsflastfm.ui.base.BaseFragment
import eu.caraus.appsflastfm.ui.search.albumdetails.AlbumDetailsFragment

class SearchAlbumsDetailsIdlingResource  : IdlingResource {

    private var resourceCallback: IdlingResource.ResourceCallback? = null
    private var isIdle: Boolean = false

    override fun getName(): String = SearchArtistListIdlingResource::class.java.name

    override fun isIdleNow(): Boolean {

        if( isIdle )return true

        val activity = getCurrentActivity()

        val fragment = activity?.supportFragmentManager?.findFragmentById( R.id.search_fragment_container ) as BaseFragment?

        fragment?.let {

            isIdle = (it is AlbumDetailsFragment)

            var albumName = it.activity?.findViewById<NestedScrollView>(R.id.nsvAlbumDetails)?.
                                                  findViewById<TextView>(R.id.tvAlbumName)

            var artistName= it.activity?.findViewById<NestedScrollView>(R.id.nsvAlbumDetails)?.
                                                  findViewById<TextView>(R.id.tvArtistName)

            isIdle = isIdle && albumName?.visibility == View.VISIBLE && artistName?.visibility == View.VISIBLE

            albumName?.let {
                isIdle = !it.text.toString().equals( it.resources.getString(R.string.album_name_placeholder), true)
            } ?: run { isIdle = false}

            artistName?.let {
                isIdle = !it.text.toString().equals( it.resources.getString(R.string.album_by_artist_placeholder), true)
            } ?: run { isIdle = false}

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
