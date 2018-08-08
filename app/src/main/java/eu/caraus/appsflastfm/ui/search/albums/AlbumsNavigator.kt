package eu.caraus.appsflastfm.ui.search.albums

import eu.caraus.appsflastfm.ui.search.SearchActivityScreenFlow

class AlbumsNavigator( private val navigation: SearchActivityScreenFlow ) : AlbumsContract.Navigator {


    override fun showAlbumDetails(artistName: String, albumName: String) {
        navigation.navigateToAlbumDetails(artistName,albumName)
    }

    override fun goBack(): Boolean {
        return navigation.goBack()
    }

}