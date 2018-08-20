package eu.caraus.appsflastfm.ui.main.albums

import android.widget.ImageView
import eu.caraus.appsflastfm.ui.base.BaseActivity
import eu.caraus.appsflastfm.ui.main.MainActivityScreenLoader


class AlbumsNavigator( private val navigation: MainActivityScreenLoader ) : AlbumsContract.Navigator {


    override fun showAlbumDetails( mbid: String, imageUrl : String, view: ImageView) {
        navigation.navigateToAlbumDetails( mbid, imageUrl, view)
    }

    override fun showSearchResultScreen( activity: BaseActivity, searchTerm: String) {
        navigation.navigateToSearchWithResult( activity, searchTerm )
    }

    override fun showAlbumDetails( mbid: String) {
        navigation.navigateToAlbumDetails( mbid )
    }

    override fun showSearchResultScreen( searchTerm: String) {
        navigation.navigateToSearchResult( searchTerm )
    }

    override fun goBack(): Boolean {
        return navigation.goBack()
    }

}