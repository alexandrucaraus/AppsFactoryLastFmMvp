package eu.caraus.appsflastfm.ui.search

import android.content.Context
import android.support.annotation.IdRes
import android.support.transition.Fade
import android.support.v4.app.FragmentManager
import android.support.v4.view.ViewCompat
import android.widget.ImageView
import eu.caraus.appsflastfm.ui.base.BaseActivity
import eu.caraus.appsflastfm.ui.base.BaseFragment
import eu.caraus.appsflastfm.ui.main.albums.AlbumsTransition
import eu.caraus.appsflastfm.ui.search.albumdetails.AlbumDetailsFragment
import eu.caraus.appsflastfm.ui.search.albums.AlbumsFragment
import eu.caraus.appsflastfm.ui.search.artists.ArtistsFragment
import java.lang.ref.WeakReference

class SearchActivityScreenLoader(activity: BaseActivity, @param:IdRes @field:IdRes private val containerId: Int) {

    private val refContext: WeakReference<Context> = WeakReference( activity )

    private val fragmentManager: FragmentManager
        get() = (context() as BaseActivity).supportFragmentManager

    private fun context(): Context? {
        return refContext.get()
    }

    fun navigateToArtists( searchedArtist : String ){
        loadFragment( ArtistsFragment.newInstance( searchedArtist))
    }

    fun navigateToTopAlbums ( artistId : String ) {
        loadFragment( AlbumsFragment.newInstance(artistId))
    }

    fun navigateToAlbumDetails ( artistName : String, albumName : String ) {
        loadFragment( AlbumDetailsFragment.newInstance( artistName, albumName ))
    }

    fun navigateToAlbumDetails ( artistName : String, albumName : String , sharedPicUrl : String, sharedElement: ImageView) {
        loadFragmentWithSharedElement(
                AlbumDetailsFragment.newInstance( artistName, albumName, sharedPicUrl,
                        ViewCompat.getTransitionName( sharedElement )), sharedElement)
    }


    fun goBack() : Boolean {
        if( fragmentManager.backStackEntryCount <= 1 )
            return true
        else
            fragmentManager.popBackStackImmediate()
        return false
    }

    private fun loadFragment( fragment : BaseFragment ) {

        val currentFragment = fragmentManager.findFragmentById( containerId )

        val transaction = fragmentManager.beginTransaction()

        currentFragment?.let {
            transaction.hide(it)
        }

        transaction.add( containerId, fragment)
        transaction.addToBackStack( null )
        transaction.commit()

    }

    private fun loadFragmentWithSharedElement( fragment: BaseFragment, sharedElement : ImageView) {

        val currentFragment = fragmentManager.findFragmentById( containerId )

        fragment.sharedElementEnterTransition  = AlbumsTransition()
        fragment.enterTransition = Fade()

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

}
