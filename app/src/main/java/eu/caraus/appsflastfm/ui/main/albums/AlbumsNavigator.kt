package eu.caraus.appsflastfm.ui.main.albums

import eu.caraus.appsflastfm.ui.main.MainActivityScreenLoader


class AlbumsNavigator( private val navigation: MainActivityScreenLoader ) : AlbumsContract.Navigator {


    override fun showAlbumDetails( mbid: String) {
        navigation.navigateToAlbumDetails( mbid )
    }

    override fun goBack(): Boolean {
        return navigation.goBack()
    }

}