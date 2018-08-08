package eu.caraus.appsflastfm.ui.search.artists

import eu.caraus.appsflastfm.ui.search.SearchActivityScreenFlow

class ArtistsNavigator( private val navigation : SearchActivityScreenFlow ) : ArtistsContract.Navigator {

    override fun showTopAlbums( artistId: String) {
        navigation.navigateToTopAlbums( artistId )
    }

    override fun goBack(): Boolean {
        return navigation.goBack()
    }

}