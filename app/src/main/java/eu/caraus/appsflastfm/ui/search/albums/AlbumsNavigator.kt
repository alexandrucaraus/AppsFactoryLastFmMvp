package eu.caraus.appsflastfm.ui.search.albums

import eu.caraus.appsflastfm.ui.search.SearchActivityScreenLoader

class AlbumsNavigator( private val navigation: SearchActivityScreenLoader ) : AlbumsContract.Navigator {


    override fun showAlbumDetails(artistName: String, albumName: String) {
        navigation.navigateToAlbumDetails(artistName,albumName)
    }

    override fun goBack(): Boolean {
        return navigation.goBack()
    }

}