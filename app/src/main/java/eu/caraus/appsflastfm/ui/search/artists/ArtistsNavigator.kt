package eu.caraus.appsflastfm.ui.search.artists

import eu.caraus.appsflastfm.ui.search.SearchActivityScreenLoader

class ArtistsNavigator( private val navigation : SearchActivityScreenLoader ) : ArtistsContract.Navigator {

    override fun showTopAlbums( artistId: String) {
        navigation.navigateToTopAlbums( artistId )
    }

    override fun goBack(): Boolean {
        return navigation.goBack()
    }

}