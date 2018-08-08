package eu.caraus.appsflastfm.ui.main

import android.content.Context
import android.content.Intent
import android.support.annotation.IdRes

import android.support.v4.app.FragmentManager
import eu.caraus.appsflastfm.ui.base.BaseActivity
import eu.caraus.appsflastfm.ui.base.BaseFragment
import eu.caraus.appsflastfm.ui.main.albums.AlbumsFragment
import eu.caraus.appsflastfm.ui.search.SearchActivity


import java.lang.ref.WeakReference

/**
 *  This class is used to perform the actual navigation,
 *  either by starting activities or loading specific fragment,
 *  it lives as long as the MainActivity lives
 *
 */

class MainActivityScreenFlow(activity: BaseActivity, @param:IdRes @field:IdRes private val containerId: Int) {

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
        val intent = Intent( context(), SearchActivity::class.java)
        intent.putExtra("SEARCH_TERM", searchTerm)
        context()?.startActivity(intent)
    }

    fun navigateToSavedAlbums(){
        loadFragment( AlbumsFragment.newInstance() )
    }

    fun navigateToAlbumDetails( artistName: String, albumName : String ){
        //loadFragment()
    }

    private fun loadFragment( fragment: BaseFragment ) {

        val currentFragment = fragmentManager.findFragmentById( containerId )

        val transaction = fragmentManager.beginTransaction()

        currentFragment?.let {
            transaction.hide(it)
        }

        transaction.add( containerId, fragment)
        transaction.addToBackStack( null )
        transaction.commit()

    }



}