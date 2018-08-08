package eu.caraus.appsflastfm.ui.main.albums

import eu.caraus.appsflastfm.ui.main.MainActivityScreenFlow


class AlbumsNavigator( private val navigation: MainActivityScreenFlow ) : AlbumsContract.Navigator {


    override fun showAlbumDetails(artistName: String, albumName: String) {
        navigation.navigateToAlbumDetails( artistName, albumName )
    }

    override fun goBack(): Boolean {
        return navigation.goBack()
    }

}