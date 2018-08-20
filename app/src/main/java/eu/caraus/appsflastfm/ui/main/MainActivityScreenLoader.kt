package eu.caraus.appsflastfm.ui.main

import android.content.Context
import android.content.Intent
import android.support.annotation.IdRes

import android.support.v4.app.FragmentManager
import android.support.v4.view.ViewCompat
import android.widget.ImageView
import eu.caraus.appsflastfm.services.youtube.YoutubePlayerService
import eu.caraus.appsflastfm.ui.base.BaseActivity
import eu.caraus.appsflastfm.ui.base.BaseFragment
import eu.caraus.appsflastfm.ui.main.albumdetails.AlbumDetailsFragment
import eu.caraus.appsflastfm.ui.main.albums.AlbumsFragment
import eu.caraus.appsflastfm.ui.main.albums.AlbumsTransition
import eu.caraus.appsflastfm.ui.search.SearchActivity
import eu.caraus.appsflastfm.ui.search.SearchActivity.Companion.SEARCH_TERM


import java.lang.ref.WeakReference

/**
 *  This class is used to perform the actual navigation,
 *  either by starting activities or loading specific fragment,
 *  it lives as long as the MainActivity lives
 *
 */

class MainActivityScreenLoader(activity: BaseActivity, @param:IdRes @field:IdRes private val containerId: Int) {

    companion object {
        const val SEARCH_REQUEST = 69
    }

    private val refContext: WeakReference<Context> = WeakReference( activity )

    private val fragmentManager: FragmentManager
        get() = (context() as BaseActivity).supportFragmentManager

    private fun context(): Context? {
        return refContext.get()
    }

    fun goBack() : Boolean {
        if( fragmentManager.backStackEntryCount <= 1 )
            return true
        else
            fragmentManager.popBackStackImmediate()
        return false
    }

    fun navigateToSearchResult( searchTerm : String ){
//        val intent = Intent( context(), SearchActivity::class.java)
//        intent.putExtra( SEARCH_TERM, searchTerm )
//        context()?.startActivity(intent)
    }

    fun navigateToSearchWithResult( activity : BaseActivity , searchTerm: String){
        val intent = Intent( context(), SearchActivity::class.java)
        intent.putExtra( SEARCH_TERM, searchTerm )
        activity.startActivityForResult( intent, SEARCH_REQUEST )
    }

    fun navigateToSavedAlbums(){
        loadFragment( AlbumsFragment.newInstance())
    }

    fun navigateToAlbumDetails( mbid : String ){
        loadFragment( AlbumDetailsFragment.newInstance( mbid ))
    }

    fun navigateToAlbumDetails( mbid : String, imageUrl: String,  view : ImageView ){
        loadFragmentWithSharedElement( AlbumDetailsFragment.newInstance( mbid , imageUrl, ViewCompat.getTransitionName(view)), view )
    }

    private fun loadFragment( fragment: BaseFragment ) {

        val currentFragment = fragmentManager.findFragmentById( containerId )

        val transaction = fragmentManager.beginTransaction()

        currentFragment?.let {
            transaction.hide(it)
        }

        transaction.setReorderingAllowed( true )
        transaction.add( containerId, fragment)
        transaction.addToBackStack( null )
        transaction.commit()

    }

    private fun loadFragmentWithSharedElement( fragment: BaseFragment, sharedElement : ImageView ) {

        val currentFragment = fragmentManager.findFragmentById( containerId )

        fragment.sharedElementEnterTransition  = AlbumsTransition()

        val transaction = fragmentManager.beginTransaction()

        currentFragment?.let {
            transaction.hide(it)
        }

        transaction.setReorderingAllowed( true )
        transaction.addSharedElement( sharedElement , ViewCompat.getTransitionName( sharedElement ))
        transaction.add( containerId, fragment)
        transaction.addToBackStack( null )
        transaction.commit()

    }

    fun startMusicService(){
        context()?.let {
            it.startService( Intent(it,YoutubePlayerService::class.java))
        }
    }

    fun stopMusicService(){
        context()?.let {
            it.stopService( Intent(it,YoutubePlayerService::class.java))
        }
    }

}